package com.minecraft.ultikits.inventoryapi;

public enum ViewType {
    PREVIOUS_BACK_NEXT(true, false, true, true, BackGround.GRAY, Buttons.BACK, 1),
    PREVIOUS_QUIT_NEXT(true, false, true, true, BackGround.GRAY, Buttons.QUIT, 1),
    OK_CANCEL(true, true, false, false, BackGround.GRAY, null, 0);

    private final boolean isPageButtonEnabled;
    private final boolean autoAddPage;
    private final BackGround backGroundColor;
    private final Buttons middleButton;
    private final int pageNumber;
    private final boolean isOkCancelEnabled;
    private final boolean isLastLineEnabled;

    ViewType(boolean isLastLineDisabled, boolean isOkCancelEnabled, boolean isPageButtonEnabled, boolean autoAddPage, BackGround backGroundColor, Buttons middleButton, int pageNumber) {
        this.isLastLineEnabled = isLastLineDisabled;
        this.isOkCancelEnabled = isOkCancelEnabled;
        this.isPageButtonEnabled = isPageButtonEnabled;
        this.autoAddPage = autoAddPage;
        this.backGroundColor = backGroundColor;
        this.middleButton = middleButton;
        this.pageNumber = pageNumber;
    }

    public boolean isPageButtonEnabled() {
        return isPageButtonEnabled;
    }

    public boolean isAutoAddPage() {
        return autoAddPage;
    }

    public BackGround getBackGroundColor() {
        return backGroundColor;
    }

    public Buttons getMiddleButton() {
        return middleButton;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean isLastLineEnabled() {
        return isLastLineEnabled;
    }

    public boolean isOkCancelEnabled() {
        return isOkCancelEnabled;
    }
}
