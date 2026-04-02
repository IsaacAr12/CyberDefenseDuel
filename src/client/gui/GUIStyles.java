package client.gui;

public final class GUIStyles {

    private GUIStyles() {}

    public static final String ROOT =
            "-fx-background-color: linear-gradient(to bottom, #0f172a, #1e293b);" +
            "-fx-font-family: 'Segoe UI';";

    public static final String TITLE =
            "-fx-text-fill: white;" +
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;";

    public static final String SUBTITLE =
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;";

    public static final String LABEL =
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;";

    public static final String ERROR =
            "-fx-text-fill: #ff6b6b;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;";

    public static final String SUCCESS =
            "-fx-text-fill: #7CFC98;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;";

    public static final String BUTTON =
            "-fx-background-color: #2563eb;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 18 10 18;";

    public static final String FIELD =
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 6;";
}