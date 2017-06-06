package com.marijannovak.littletanks;

/**
 * Created by marij on 1.6.2017..
 */

public class SettingItem {

    private String settingName;
    private Boolean hasCheck, checked;

    public SettingItem(String name)
    {
        this.settingName = name;
        this.hasCheck = false;
    }

    public SettingItem(String name, Boolean chck)
    {
        this.settingName = name;
        this.hasCheck = true;
        this.checked = chck;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String name)
    {
        this.settingName = name;
    }

    public Boolean getChecked(){

        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getHasCheck()
    {
        return this.hasCheck;
    }
}
