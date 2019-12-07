package com.example.tetraconstraintdemo.managers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginManager {

    private static LoginManager manager;

    public static  synchronized LoginManager getInstance(){
        if(manager == null){
            manager = new LoginManager();
        }


        return manager;
    }

    private LoginManager(){



    }

    public boolean isLoggedIn(){

        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }


    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }





}
