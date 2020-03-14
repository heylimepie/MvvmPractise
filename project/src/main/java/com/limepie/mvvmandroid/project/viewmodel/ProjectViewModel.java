package com.limepie.mvvmandroid.project.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.limepie.mvvmandroid.base.viewmodel.BaseViewModel;
import com.limepie.mvvmandroid.project.data.ProjectRepository;
import com.limepie.mvvmandroid.project.model.Project;

import java.util.List;

public class ProjectViewModel extends BaseViewModel {
    ProjectRepository repository = new ProjectRepository();

    private MutableLiveData<List<Project>> project;

    public LiveData<List<Project>> getProjects() {
        if (project == null) {
            loadProject();
        }
        return project;
    }

    private void loadProject() {
        if (repository.cacheValidate()) {
            project= repository.getLocalData();
        } else {
            project = repository.getRemoteData();
        }
    }
}
