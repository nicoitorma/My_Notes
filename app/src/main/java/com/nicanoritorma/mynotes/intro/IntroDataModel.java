package com.nicanoritorma.mynotes.intro;


public class IntroDataModel {

    int intro_iv;
    String intro_tv;

    public IntroDataModel() {
    }

    public IntroDataModel(int intro_iv, String intro_tv) {
        this.intro_iv = intro_iv;
        this.intro_tv = intro_tv;
    }

    public int getIntro_iv() {
        return intro_iv;
    }

    public void setIntro_iv(int intro_iv) {
        this.intro_iv = intro_iv;
    }

    public String getIntro_tv() {
        return intro_tv;
    }

    public void setIntro_tv(String intro_tv) {
        this.intro_tv = intro_tv;
    }
}
