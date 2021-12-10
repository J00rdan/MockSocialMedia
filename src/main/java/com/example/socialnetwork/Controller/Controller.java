package com.example.socialnetwork.Controller;

import com.example.socialnetwork.GUI;
import com.example.socialnetwork.Service.Service;

public class Controller {
    protected Service srv;
    protected GUI gui;

    public void setService(Service service, GUI gui){
        this.srv = service;
        this.gui = gui;
    }
}
