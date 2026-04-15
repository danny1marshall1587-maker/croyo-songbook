package com.garethevans.church.opensongtablet.screensetup;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garethevans.church.opensongtablet.R;
import com.garethevans.church.opensongtablet.customviews.ExposedDropDownArrayAdapter;
import com.garethevans.church.opensongtablet.databinding.SettingsThemeBinding;
import com.garethevans.church.opensongtablet.interfaces.DisplayInterface;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;

import java.util.ArrayList;

public class ThemeSetupFragment extends Fragment {

    @SuppressWarnings({"unused","FieldCanBeLocal"})
    private final String TAG = "ThemeSetupFragment";
    private MainActivityInterface mainActivityInterface;
    private DisplayInterface displayInterface;
    private SettingsThemeBinding myView;

    private String myTheme, theme_string="", website_themes_string="", presenter_mode_string="",
            stage_mode_string="", theme_dark_string="", theme_light_string="",
            theme_custom1_string="", theme_custom2_string="",
            theme_arctic_string="", theme_sepia_string="", theme_night_string="",
            theme_emerald_string="",
            reset_colours_string="",
            recreate_string="", this_deeplink="";
    private ArrayList<String> themes;
    private String webAddress;
    private String initialTheme;
    private ExposedDropDownArrayAdapter arrayAdapter, elementAdapter;
    private ArrayList<String> elements;
    private String[] elementKeys;
    private int selectedElementIndex = 0;

    @Override
    public void onResume() {
        super.onResume();
        mainActivityInterface.updateToolbar(theme_string);
        mainActivityInterface.updateToolbarHelp(webAddress);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivityInterface = (MainActivityInterface) context;
        displayInterface = (DisplayInterface) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = SettingsThemeBinding.inflate(inflater,container,false);

        //myView.getRoot().setBackgroundColor(mainActivityInterface.getPalette().background);

        prepareStrings();
        webAddress = website_themes_string;

        // Initialise the element picker BEFORE themes to ensure elementKeys exists
        setUpElementPicker();

        // Initialise the themes
        setUpTheme();

        // Set the button listeners
        setListeners();

        return myView.getRoot();
    }

    private void prepareStrings() {
        if (getContext()!=null) {
            theme_string = getString(R.string.theme);
            website_themes_string = getString(R.string.website_themes);
            presenter_mode_string = getString(R.string.presenter_mode);
            stage_mode_string = getString(R.string.stage_mode);
            theme_dark_string = getString(R.string.theme_dark);
            theme_light_string = getString(R.string.theme_light);
            theme_custom1_string = getString(R.string.theme_custom1);
            theme_custom2_string = getString(R.string.theme_custom2);
            theme_arctic_string = getString(R.string.theme_dyslexia_arctic);
            theme_sepia_string = getString(R.string.theme_dyslexia_sepia);
            theme_night_string = getString(R.string.theme_dyslexia_night);
            theme_emerald_string = getString(R.string.theme_dyslexia_emerald);
            reset_colours_string = getString(R.string.reset_colours);
            recreate_string = getString(R.string.restart_auto);
            initialTheme = mainActivityInterface.getPreferences().getMyPreferenceString("appTheme","");
            this_deeplink = getString(R.string.deeplink_theme);
        }
    }

    private void setUpTheme() {
        themes = new ArrayList<>();
        themes.add(theme_dark_string);
        themes.add(theme_light_string);
        themes.add(theme_custom1_string);
        themes.add(theme_custom2_string);
        themes.add(theme_arctic_string);
        themes.add(theme_sepia_string);
        themes.add(theme_night_string);
        themes.add(theme_emerald_string);

        arrayAdapter = null;
        if (getContext() != null) {
            arrayAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.themeName, R.layout.view_exposed_dropdown_item, themes);
        }
        // myTheme defaults to the current light/dark mode on the device if not set
        myTheme = mainActivityInterface.getMyThemeColors().getThemeName();
        switch (myTheme) {
            case "light":
                myView.themeName.setText(themes.get(1));
                break;
            case "custom1":
                myView.themeName.setText(themes.get(2));
                break;
            case "custom2":
                myView.themeName.setText(themes.get(3));
                break;
            case "dyslexia_arctic":
                myView.themeName.setText(themes.get(4));
                break;
            case "dyslexia_sepia":
                myView.themeName.setText(themes.get(5));
                break;
            case "dyslexia_night":
                myView.themeName.setText(themes.get(6));
                break;
            case "dyslexia_emerald":
                myView.themeName.setText(themes.get(7));
                break;
            case "dark":
            default:
                myView.themeName.setText(themes.get(0));
                break;
        }

        myView.themeName.postDelayed(() -> {
                    if (myView == null) return;
                    myView.themeName.setAdapter(arrayAdapter);
                    myView.themeName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s != null && s.toString().equals(themes.get(0))) {
                                myTheme = "dark";
                            } else if (s != null && s.toString().equals(themes.get(1))) {
                                myTheme = "light";
                            } else if (s != null && s.toString().equals(themes.get(2))) {
                                myTheme = "custom1";
                            } else if (s != null && s.toString().equals(themes.get(3))) {
                                myTheme = "custom2";
                            } else if (s != null && s.toString().equals(themes.get(4))) {
                                myTheme = "dyslexia_arctic";
                            } else if (s != null && s.toString().equals(themes.get(5))) {
                                myTheme = "dyslexia_sepia";
                            } else if (s != null && s.toString().equals(themes.get(6))) {
                                myTheme = "dyslexia_night";
                            } else if (s != null && s.toString().equals(themes.get(7))) {
                                myTheme = "dyslexia_emerald";
                            }

                            if (getContext() != null) {
                                mainActivityInterface.getPalette().savePref(getContext(), myTheme.equals("dark") || myTheme.equals("custom1"));
                                mainActivityInterface.getToolbar().changeTheme();
                                mainActivityInterface.getBatteryStatus().getBatteryStatus();
                                mainActivityInterface.tintBackgroundToTheme();
                            }

                            mainActivityInterface.getPreferences().setMyPreferenceString("appTheme", myTheme);

                            updateColors();
                            updateButtons();
                            // Also update secondary screen
                            displayInterface.updateDisplay("setSongContentPrefs");

                            if (checkNeedsRefresh()) {
                                // Try to redraw the current views
                                invalidateViews();
                            }
                            initialTheme = myTheme;
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                },200);

        updateColors();
        updateButtons();
        updateChips();
    }

    private void setUpElementPicker() {
        elements = new ArrayList<>();
        // Group 1: Main Song Display
        elements.add(getString(R.string.lyrics));
        elements.add(getString(R.string.chords));
        elements.add(getString(R.string.capo_chords));
        elements.add(getString(R.string.multilingual));
        elements.add(getString(R.string.background));
        
        // Group 2: Section Highlighting
        elements.add(getString(R.string.verse));
        elements.add(getString(R.string.chorus));
        elements.add(getString(R.string.bridge));
        elements.add(getString(R.string.prechorus));
        elements.add(getString(R.string.tag));
        elements.add(getString(R.string.comment));
        elements.add(getString(R.string.custom));
        elements.add(getString(R.string.title));
        
        // Group 3: Presenter/Stage
        elements.add(getString(R.string.text) + " (" + presenter_mode_string + ")");
        elements.add(getString(R.string.chords) + " (" + presenter_mode_string + ")");
        elements.add(getString(R.string.info_text));
        elements.add(getString(R.string.alert));
        elements.add(getString(R.string.block_text_shadow));
        
        // Group 4: Extras
        elements.add(getString(R.string.metronome));
        elements.add(getString(R.string.hot_zones));
        elements.add(getString(R.string.focal_tracker_color));

        elementKeys = new String[]{
            "lyricsTextColor", "lyricsChordsColor", "lyricsCapoColor", "multilingualTextColor", "lyricsBackgroundColor",
            "lyricsVerseColor", "lyricsChorusColor", "lyricsBridgeColor", "lyricsPreChorusColor", "lyricsTagColor", "lyricsCommentColor", "lyricsCustomColor", "highlightHeadingColor",
            "presoFontColor", "presoChordColor", "presoInfoFontColor", "presoAlertColor", "presoShadowColor",
            "metronomeColor", "hotZoneColor", "focalTrackerColor"
        };

        if (getContext() != null) {
            elementAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.elementPicker, R.layout.view_exposed_dropdown_item, elements);
            myView.elementPicker.setAdapter(elementAdapter);
            myView.elementPicker.setText(elements.get(0));
            
            myView.elementPicker.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectedElementIndex = elements.indexOf(s.toString());
                    updateCustomColorPreview();
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
        
        myView.customColorButton.setOnClickListener(v -> {
            if (selectedElementIndex >= 0 && selectedElementIndex < elementKeys.length) {
                chooseColor(elementKeys[selectedElementIndex]);
            }
        });
        
        updateCustomColorPreview();
    }

    private void updateCustomColorPreview() {
        if (elementKeys != null && selectedElementIndex >= 0 && selectedElementIndex < elementKeys.length) {
            String key = elementKeys[selectedElementIndex];
            int color;
            if (key.equals("focalTrackerColor")) {
                color = mainActivityInterface.getPreferences().getMyPreferenceInt(key, android.graphics.Color.parseColor("#FF4081"));
            } else {
                color = mainActivityInterface.getMyThemeColors().getValue(key);
            }
            myView.customColorButton.setColor(color);
            myView.customColorButton.setText(getString(R.string.pick_color) + ": " + elements.get(selectedElementIndex));
        }
    }

    public void updateColors() {
        Log.d(TAG,"updateColors()  myTheme:"+myTheme+"  initialTheme:"+initialTheme);
        mainActivityInterface.getMyThemeColors().getDefaultColors();
    }

    public void updateButtons() {
        myView.invertPDF.setChecked(mainActivityInterface.getMyThemeColors().getInvertPDF());

        myView.pageButton.setColor(mainActivityInterface.getMyThemeColors().getLyricsBackgroundColor());
        myView.lyricsButton.setColor(mainActivityInterface.getMyThemeColors().getLyricsTextColor());
        myView.chordsButton.setColor(mainActivityInterface.getMyThemeColors().getLyricsChordsColor());

        myView.presoButton.setColor(mainActivityInterface.getMyThemeColors().getPresoFontColor());
        myView.presoChordButton.setColor(mainActivityInterface.getMyThemeColors().getPresoChordColor());

        updateCustomColorPreview();
        updateChips();
    }

    private void updateChips() {
        myView.chipVerse.setColor(mainActivityInterface.getMyThemeColors().getLyricsVerseColor());
        myView.chipChorus.setColor(mainActivityInterface.getMyThemeColors().getLyricsChorusColor());
        myView.chipBridge.setColor(mainActivityInterface.getMyThemeColors().getLyricsBridgeColor());
    }

    private void setListeners() {
        myView.invertPDF.setOnCheckedChangeListener((buttonView, isChecked) -> mainActivityInterface.getMyThemeColors().setInvertPDF(isChecked));
        myView.lyricsButton.setOnClickListener(v-> chooseColor("lyricsTextColor"));
        myView.presoButton.setOnClickListener(v-> chooseColor("presoFontColor"));
        myView.presoChordButton.setOnClickListener(v-> chooseColor("presoChordColor"));
        myView.chordsButton.setOnClickListener(v-> chooseColor("lyricsChordsColor"));
        myView.pageButton.setOnClickListener(v-> chooseColor("lyricsBackgroundColor"));

        myView.resetTheme.setOnClickListener(v -> mainActivityInterface.displayAreYouSure("resetColors",myView.themeName.getText().toString() + ": "+reset_colours_string,null,"themeSetupFragment",this,null));

        myView.chipVerse.setOnClickListener(v -> chooseColor("lyricsVerseColor"));
        myView.chipChorus.setOnClickListener(v -> chooseColor("lyricsChorusColor"));
        myView.chipBridge.setOnClickListener(v -> chooseColor("lyricsBridgeColor"));
    }

    private void chooseColor(String which) {
        // This moves to the color chooser bottom sheet dialog fragment
        ChooseColorBottomSheet chooseColorBottomSheet = new ChooseColorBottomSheet(this,"themeSetupFragment",which);
        chooseColorBottomSheet.show(mainActivityInterface.getMyFragmentManager(),"ChooseColorBottomSheet");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getContext()!=null) {
            mainActivityInterface.getPalette().savePref(getContext(), myTheme.equals("dark") || myTheme.equals("custom1"));
        }
        myView = null;
    }

    private boolean checkNeedsRefresh() {
        // Compare the initial theme with the new one
        // dark and custom1 are dark based, light and custom2 are light based
        boolean refreshActivity = false;
        String currentTheme = mainActivityInterface.getPreferences().getMyPreferenceString("appTheme","dark");
        Log.d(TAG,"currentTheme:"+currentTheme+"  initialTheme:"+initialTheme);
        if (currentTheme.equals("dark") || currentTheme.equals("custom1")) {
            refreshActivity = initialTheme.equals("light") || initialTheme.equals("custom2") || initialTheme.isEmpty();
        } else if (currentTheme.equals("light") || currentTheme.equals("custom2")) {
            refreshActivity = initialTheme.equals("dark") || initialTheme.equals("custom1") || initialTheme.isEmpty();
        }
        Log.d(TAG,"refreshActivity:"+refreshActivity);
        return refreshActivity;
    }

    private void invalidateViews() {
        mainActivityInterface.getMainHandler().post(() -> {
            if (myView == null) return;
            myView.getRoot().setBackgroundColor(mainActivityInterface.getPalette().background);
            myView.invertPDF.setPalette(mainActivityInterface.getPalette());
            myView.resetTheme.setPalette(mainActivityInterface.getPalette());
            myView.themeName.setPalette(mainActivityInterface.getPalette());
            myView.lyricsButton.setPalette(mainActivityInterface.getPalette());
            myView.chordsButton.setPalette(mainActivityInterface.getPalette());
            myView.pageButton.setPalette(mainActivityInterface.getPalette());

            myView.presoButton.setPalette(mainActivityInterface.getPalette());
            myView.presoChordButton.setPalette(mainActivityInterface.getPalette());

            myView.elementPicker.setPalette(mainActivityInterface.getPalette());
            myView.customColorButton.setPalette(mainActivityInterface.getPalette());
            myView.chipVerse.setPalette(mainActivityInterface.getPalette());
            myView.chipChorus.setPalette(mainActivityInterface.getPalette());
            myView.chipBridge.setPalette(mainActivityInterface.getPalette());
        });
    }

}
