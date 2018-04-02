package com.chaoxing.pdfreader;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.artifex.mupdf.fitz.Page;

import java.util.List;

/**
 * Created by HUWEI on 2018/3/26.
 */

public class DocumentViewModel extends AndroidViewModel {

    private DocumentHelper mDocumentHelper = new DocumentHelper();

    private final MutableLiveData<String> mPath = new MutableLiveData<>();
    private LiveData<Resource<DocumentBinding>> mOpenDocumentResult;

    private final MutableLiveData<String> mPassword = new MutableLiveData<>();
    private LiveData<Resource<Boolean>> mCheckPasswordResult;

    private final MutableLiveData<DocumentBinding> mLoadDocument = new MutableLiveData<>();
    private LiveData<Resource<DocumentBinding>> mLoadDocumentResult;

    private final MutableLiveData<Integer> mLoadPage = new MutableLiveData<>();
    private LiveData<Resource<Page>> mLoadPageResult;


    public DocumentViewModel(@NonNull Application application) {
        super(application);
        mOpenDocumentResult = Transformations.switchMap(mPath, new Function<String, LiveData<Resource<DocumentBinding>>>() {
            @Override
            public LiveData<Resource<DocumentBinding>> apply(String documentPath) {
                return mDocumentHelper.openDocument(getApplication().getApplicationContext(), documentPath);
            }
        });

        mCheckPasswordResult = Transformations.switchMap(mPassword, new Function<String, LiveData<Resource<Boolean>>>() {
            @Override
            public LiveData<Resource<Boolean>> apply(String password) {
                return mDocumentHelper.checkPassword(getApplication().getApplicationContext(), mOpenDocumentResult.getValue().getData().getDocument(), password);
            }
        });

        mLoadDocumentResult = Transformations.switchMap(mLoadDocument, new Function<DocumentBinding, LiveData<Resource<DocumentBinding>>>() {
            @Override
            public LiveData<Resource<DocumentBinding>> apply(DocumentBinding documentBinding) {
                return mDocumentHelper.loadDocument(getApplication().getApplicationContext(), documentBinding);
            }
        });

//        mLoadPageResult = Transformations.switchMap(mLoadPageList, new Function<DocumentBinding, LiveData<Resource<List<Page>>>>() {
//            @Override
//            public LiveData<Resource<List<Page>>> apply(DocumentBinding documentBinding) {
//                return DocumentHelper.get().loadPageList(getApplication().getApplicationContext(), documentBinding);
//            }
//        });
    }


    public void openDocument(String path) {
        mPath.setValue(path);
    }

    public LiveData<Resource<DocumentBinding>> getOpenDocumentResult() {
        return mOpenDocumentResult;
    }

    public void checkPassword(final String password) {
        mPassword.setValue(password);
    }

    public LiveData<Resource<Boolean>> getCheckPasswordResult() {
        return mCheckPasswordResult;
    }

    public void loadDocument() {
        mLoadDocument.setValue(getOpenDocumentResult().getValue().getData());
    }

    public LiveData<Resource<DocumentBinding>> getLoadDocumentResult() {
        return mLoadDocumentResult;
    }

    public DocumentBinding getDocumentBinding() {
        return mLoadDocumentResult.getValue().getData();
    }

//    public List<Page> getPageList() {
//        return mLoadPageListResult.getValue().getData();
//    }

}
