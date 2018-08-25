package com.util;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

public class Fonts {


    private static String ATSackersHeavyGothic = "ATSackersHeavyGothic.ttf";
    private static String bertholdcitybold = "berthold-city-bold.ttf";
    private static String dinbold = "din-bold.ttf";
    private static String DINEngschriftRegular = "DINEngschrift-Regular.ttf";
    private static String DINEngschriftStd = "DINEngschriftStd.ttf";
    private static String dinmedium = "din-medium.ttf";
    private static String GothamBold = "gotham_bold.ttf";
    private static String GothamLigth = "gotham_light.ttf";
    private static String GothamMedium = "gotham_medium.ttf";
    private static String GillsansMedium = "gillsans_medium.ttf";
    private static String GillsansBold = "gillsans_bold.ttf";
    private static String ClarendonBold = "clarenbd.ttf";
    private static String BaileyQuadITCStdBold = "BAILEYQUADITCSTD-BOLD.OTF";
    private static String BaileySanITCStdBookRegular = "baileysansitcbook_regular.ttf";
    private static String BaileySanITCStdBookBold = "BAILEYSANSITCBOOK-BOLD.ttf";
    private static String BaileySanITCStdBookItalic = "BAILEYSANSITCBOOK-ITALIC.ttf";
    private static String BaileySanITCStdBookItalicBold = "BAILEYSANSITCBOOK-ITALIC-BOLD.ttf";
    private static String LSTKClaBol = "lstkciabol.otf";
    private static String ChronicleTextG1RomanPro = "chronicletextg1_roman_pro.otf";
    private static String ChronicleDispSemibold = "chronicledisp_semibold.otf";
    private static String ChronicleBold = "chronicledisp_bold.otf";
    private static String ChronicleTextG1RomanProItalicBold = "chronicletextg1-bolditalic.otf";

    private static String RobotoCondensedRegular = "robotocondensed_regular.ttf";
    private static String RobotoCondensedBold = "robotocondensed_bold.ttf";
    private static String FinkHeavyRegular = "finkheavy.ttf";

    public static void fontChronicleDispSemibold(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + ChronicleDispSemibold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontChronicleTextG1RomanProTextView(TextView textView, int size, String color,
                                                           AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + ChronicleTextG1RomanPro);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontChronicleTextG1RomanProBoldTextView(TextView textView, int size, String color,
                                                               AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + ChronicleTextG1RomanPro);
        setTextViewAttributeBold(textView, size, color, face);
    }

    public static void fontChronicleTextG1RomanProBoldItalicTextView(TextView textView, int size, String color,
                                                                     AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + ChronicleTextG1RomanProItalicBold);
        setTextViewAttributeBold(textView, size, color, face);
    }

    public static void fontChronicleBoldTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + ChronicleBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontLSTKClabolTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + LSTKClaBol);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontBaileyQuadITCStdBoldTextView(TextView textView, int size, String color,
                                                        AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + BaileyQuadITCStdBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontBaileySanITCStdBookRegularTextView(TextView textView, int size, String color,
                                                              AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + BaileySanITCStdBookRegular);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontBaileySanITCStdBookBoldTextView(TextView textView, int size, String color,
                                                           AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + BaileySanITCStdBookBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontBaileySanITCStdBookItalicTextView(TextView textView, int size, String color,
                                                             AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + BaileyQuadITCStdBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontBaileySanITCStdBookItalicBoldTextView(TextView textView, int size, String color,
                                                                 AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + BaileySanITCStdBookItalicBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontGillsansMediumTextView(TextView textView, int size, String color,
                                                  AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + GillsansMedium);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontGillsansBoldTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + GillsansBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontClarendonBoldTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + ClarendonBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontGothamBoldTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + GothamBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontGothamLightTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + GothamLigth);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontGothamMediumTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + GothamMedium);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontATSackersHeavyGothicTextView(TextView textView, int size, String color,
                                                        AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + ATSackersHeavyGothic);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontbertholdcityboldTextView(TextView textView, int size, String color,
                                                    AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + bertholdcitybold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontdinboldTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + dinbold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontDINEngschriftRegularTextView(TextView textView, int size, String color,
                                                        AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + DINEngschriftRegular);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontDINEngschriftStdTextView(TextView textView, int size, String color,
                                                    AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + DINEngschriftStd);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontdinmediumTextView(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + dinmedium);
        setTextViewAttribute(textView, size, color, face);
    }


    public static void fontATSackersHeavyGothicTextViewBold(TextView textView, int size, String color,
                                                            AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + ATSackersHeavyGothic);
        setTextViewAttributeBold(textView, size, color, face);
    }

    public static void fontdinmediumTextViewBold(TextView textView, int size, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + dinmedium);
        setTextViewAttributeBold(textView, size, color, face);
    }

    private static void setTextViewAttributeBold(TextView textView, int size, String color, Typeface face) {
        textView.setTextSize(size);
        color = "#" + color;
        textView.setTextColor(Color.parseColor(color));
        textView.setTypeface(face, Typeface.BOLD);
        // tv.setTextColor(Color.parseColor("#000000"))
    }

    public static void fontRobotoCodensedRegularTextView(TextView textView, int size, String color,
                                                         AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + RobotoCondensedRegular);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontRobotoCodensedBoldTextView(TextView textView, int size, String color,
                                                      AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + RobotoCondensedBold);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontFinkHeavyRegularTextView(TextView textView, int size, String color,
                                                    AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + FinkHeavyRegular);
        setTextViewAttribute(textView, size, color, face);
    }

    public static void fontFinkHeavyRegularTextView(TextView textView, String color, AssetManager assetManager) {
        Typeface face = Typeface.createFromAsset(assetManager, "" + FinkHeavyRegular);
        setTextViewAttribute(textView, color, face);
    }


    public static void setTextViewAttribute(TextView textView, int size, String color,
                                            AssetManager assetManager/* , Typeface face */) {
        textView.setTextSize(size);
        color = "#" + color;
        textView.setTextColor(Color.parseColor(color));
    }

    public static void setTextViewAttributeBold(TextView textView, int size, String color,
                                                AssetManager assetManager/* , Typeface face */) {
        textView.setTextSize(size);
        color = "#" + color;
        textView.setTextColor(Color.parseColor(color));
        textView.setTypeface(null, Typeface.BOLD);
    }


    private static void setTextViewAttribute(TextView textView, int size, String color, Typeface face) {
        textView.setTextSize(size);
        color = "#" + color;
        textView.setTextColor(Color.parseColor(color));
        textView.setTypeface(face/* ,Typeface.BOLD */);
        // tv.setTextColor(Color.parseColor("#000000"))
    }

    private static void setTextViewAttribute(TextView textView, String color, Typeface face) {
        color = "#" + color;
        textView.setTextColor(Color.parseColor(color));
        textView.setTypeface(face/* ,Typeface.BOLD */);
        // tv.setTextColor(Color.parseColor("#000000"))
    }


}
