package com.orangehrm.demo.ui;

import net.serenitybdd.screenplay.targets.Target;

public final class SideMenu {

    public static final Target ITEM = Target.the("the '{0}' menu item")
            .locatedBy("//a[contains(@class,'oxd-main-menu-item')][normalize-space()='{0}']");

    private SideMenu() {
    }
}
