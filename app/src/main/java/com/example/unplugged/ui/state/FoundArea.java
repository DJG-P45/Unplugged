package com.example.unplugged.ui.state;

import android.view.View;

public class FoundArea {

    private String title;
    private String subtitle;
    private View.OnClickListener selectAction;

    public FoundArea() {
    }

    public FoundArea(String title, String subtitle, View.OnClickListener selectAction) {
        this.title = title;
        this.subtitle = subtitle;
        this.selectAction = selectAction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public View.OnClickListener getSelectAction() {
        return selectAction;
    }

    public void setSelectAction(View.OnClickListener selectAction) {
        this.selectAction = selectAction;
    }
}
