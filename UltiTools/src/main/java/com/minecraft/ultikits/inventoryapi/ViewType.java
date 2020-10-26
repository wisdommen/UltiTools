package com.minecraft.ultikits.inventoryapi;

import com.minecraft.ultikits.enums.Colors;

/**
 * The enum View type.
 */
public enum ViewType {
    /**
     * Previous back next view type.
     */
    PREVIOUS_BACK_NEXT(true, false, true, true, Colors.GRAY, Buttons.BACK, 1),
    /**
     * Previous quit next view type.
     */
    PREVIOUS_QUIT_NEXT(true, false, true, true, Colors.GRAY, Buttons.QUIT, 1),
    /**
     * Ok cancel view type.
     */
    OK_CANCEL(true, true, false, false, Colors.GRAY, null, 0);

    private final boolean isPageButtonEnabled;
    private final boolean autoAddPage;
    private final Colors backGroundColor;
    private final Buttons middleButton;
    private final int pageNumber;
    private final boolean isOkCancelEnabled;
    private final boolean isLastLineEnabled;

    ViewType(boolean isLastLineDisabled, boolean isOkCancelEnabled, boolean isPageButtonEnabled, boolean autoAddPage, Colors backGroundColor, Buttons middleButton, int pageNumber) {
        this.isLastLineEnabled = isLastLineDisabled;
        this.isOkCancelEnabled = isOkCancelEnabled;
        this.isPageButtonEnabled = isPageButtonEnabled;
        this.autoAddPage = autoAddPage;
        this.backGroundColor = backGroundColor;
        this.middleButton = middleButton;
        this.pageNumber = pageNumber;
    }

    /**
     * Is page button enabled boolean.
     *
     * @return the boolean
     */
    public boolean isPageButtonEnabled() {
        return isPageButtonEnabled;
    }

    /**
     * Is auto add page boolean.
     *
     * @return the boolean
     */
    public boolean isAutoAddPage() {
        return autoAddPage;
    }

    /**
     * Gets back ground color.
     *
     * @return the back ground color
     */
    public Colors getBackGroundColor() {
        return backGroundColor;
    }

    /**
     * Gets middle button.
     *
     * @return the middle button
     */
    public Buttons getMiddleButton() {
        return middleButton;
    }

    /**
     * Gets page number.
     *
     * @return the page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Is last line enabled boolean.
     *
     * @return the boolean
     */
    public boolean isLastLineEnabled() {
        return isLastLineEnabled;
    }

    /**
     * Is ok cancel enabled boolean.
     *
     * @return the boolean
     */
    public boolean isOkCancelEnabled() {
        return isOkCancelEnabled;
    }
}
