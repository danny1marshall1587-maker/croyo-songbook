package com.garethevans.church.opensongtablet.screensetup;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garethevans.church.opensongtablet.R;
import com.garethevans.church.opensongtablet.customviews.ExposedDropDownArrayAdapter;
import com.garethevans.church.opensongtablet.databinding.SettingsDisplayExtraBinding;
import com.garethevans.church.opensongtablet.interfaces.DisplayInterface;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.android.material.slider.Slider;

public class DisplayExtraFragment extends Fragment {

    private MainActivityInterface mainActivityInterface;
    private DisplayInterface displayInterface;
    private SettingsDisplayExtraBinding myView;
    private String[] bracketStyles_Names;
    private int[] bracketStyles_Ints;
    @SuppressWarnings({"unused","FieldCanBeLocal"})
    private final String TAG = "DisplayExtraFrag";
    private String song_display_string="", website_song_display_string="", save_string="",
            filters_string="", format_text_normal_string="", format_text_italic_string="",
            format_text_bold_string="", format_text_bolditalic_string="",
            focal_tracker_custom_string="";
    private String[] focalColorNames;
    private String[] prompterColorNames;
    private String webAddress;

    @Override
    public void onResume() {
        super.onResume();
        mainActivityInterface.updateToolbar(song_display_string);
        mainActivityInterface.updateToolbarHelp(webAddress);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivityInterface = (MainActivityInterface) context;
        displayInterface = (DisplayInterface) mainActivityInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = SettingsDisplayExtraBinding.inflate(inflater,container,false);

        myView.getRoot().setBackgroundColor(mainActivityInterface.getPalette().background);

        prepareStrings();

        if (getActivity()!=null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        webAddress = website_song_display_string;

        // Set up views
        setViews();

        // Set up listeners
        setListeners();

        return myView.getRoot();
    }

    private void prepareStrings() {
        if (getContext()!=null) {
            song_display_string = getString(R.string.song_display);
            website_song_display_string = getString(R.string.website_song_display);
            save_string = getString(R.string.save);
            filters_string = getString(R.string.filters);
            format_text_normal_string = getString(R.string.format_text_normal);
            format_text_italic_string = getString(R.string.format_text_italic);
            format_text_bold_string = getString(R.string.format_text_bold);
            format_text_bolditalic_string = getString(R.string.format_text_bolditalic);
            focal_tracker_custom_string = getString(R.string.focal_tracker_custom);
            focalColorNames = new String[] {"Pink", "Amber", "Cyan", focal_tracker_custom_string};
            prompterColorNames = new String[] {"Gold", "Cyan", "Slate", "White", "Green", "Pink", "Amber"};
        }
    }
    private void setViews() {
        // Set the checkboxes
        myView.songSheet.setChecked(getChecked("songSheet",false));
        myView.nextInSet.setChecked(getChecked("nextInSet",true));
        myView.prevInSet.setChecked(getChecked("prevInSet",false));
        myView.prevNextSongMenu.setChecked(getChecked("prevNextSongMenu",false));
        myView.prevNextTextButtons.setChecked(getChecked("prevNextTextButtons",true));
        myView.prevNextHide.setChecked(getChecked("prevNextHide",true));
        myView.onscreenAutoscrollHide.setChecked(getChecked("onscreenAutoscrollHide",true));
        myView.onscreenCapoHide.setChecked(getChecked("onscreenCapoHide", true));
        myView.onscreenPadHide.setChecked(getChecked("onscreenPadHide",true));
        myView.boldChordsHeadings.setChecked(getChecked("displayBoldChordsHeadings",false));
        myView.boldChorus.setChecked(getChecked("displayBoldChorus",false));
        myView.showChords.setChecked(getChecked("displayChords",true));
        myView.showLyrics.setChecked(getChecked("displayLyrics",true));
        myView.presoOrder.setChecked(getChecked("usePresentationOrder",false));
        myView.keepMultiline.setChecked(getChecked("multiLineVerseKeepCompact",false));
        myView.trimSections.setChecked(getChecked("trimSections",true));
        myView.addSectionSpace.setChecked(getChecked("addSectionSpace",true));
        myView.trimLineSpacing.setChecked(getChecked("trimLines",false));
        visibilityByBoolean(myView.trimLineSpacingSlider,myView.trimLineSpacing.getChecked());
        float lineSpacing = mainActivityInterface.getPreferences().getMyPreferenceFloat("lineSpacing",0.1f);
        int percentage = (int)(lineSpacing * 100);
        myView.trimLineSpacingSlider.setValue(percentage);
        myView.trimLineSpacingSlider.setLabelFormatter(value -> ((int)value)+"%");
        sliderValToText(percentage);
        myView.trimWordSpacing.setChecked(getChecked("trimWordSpacing", true));
        myView.dyslexiaMode.setChecked(getChecked("dyslexiaMode", false));
        myView.highContrastLyrics.setChecked(getChecked("highContrastLyrics", false));
        myView.voiceControl.setChecked(getChecked("voiceControlEnabled", false));
        float masterT = mainActivityInterface.getPreferences().getMyPreferenceFloat("jsxMasterThreshold", -60.0f);
        myView.jsxMasterThreshold.setValue(masterT);
        myView.jsxMasterThresholdLabel.setText("JSx Master Threshold: " + (int)masterT + " dB");
        // JSx Calibrate button doesn't need checked state
        // TODO Maybe add later
        // myView.addSectionBox.setChecked(getChecked("addSectionBox",false));
        myView.filterSwitch.setChecked(getChecked("filterSections",false));
        visibilityByBoolean(myView.filterLayout,myView.filterSwitch.getChecked());
        myView.filterShow.setChecked(getChecked("filterShow",false));
        String text = save_string + " (" + filters_string + ")";
        myView.filterSave.setText(text);
        mainActivityInterface.getProcessSong().editBoxToMultiline(myView.filters);
        mainActivityInterface.getProcessSong().stretchEditBoxToLines(myView.filters,4);
        myView.filters.setText(mainActivityInterface.getPreferences().getMyPreferenceString("filterText",""));
        bracketStyles_Names = new String[] {format_text_normal_string,format_text_italic_string,
                format_text_bold_string,format_text_bolditalic_string};
        bracketStyles_Ints = new int[] {Typeface.NORMAL,Typeface.ITALIC,Typeface.BOLD,Typeface.BOLD_ITALIC};
        if (getContext()!=null) {
            ExposedDropDownArrayAdapter exposedDropDownArrayAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.bracketsStyle, R.layout.view_exposed_dropdown_item, bracketStyles_Names);
            myView.bracketsStyle.setAdapter(exposedDropDownArrayAdapter);
        }
        myView.bracketsStyle.setText(getBracketStringFromValue(mainActivityInterface.getPreferences().getMyPreferenceInt("bracketsStyle",Typeface.NORMAL)));
        myView.curlyBrackets.setChecked(getChecked("curlyBrackets",true));
        myView.curlyBracketsDevice.setChecked(getChecked("curlyBracketsDevice",false));
        myView.pdfHorizontal.setChecked(getChecked("pdfLandscapeView",true));

        // Focal Flip & Bluetooth Shield
        myView.bluetoothShield.setChecked(getChecked("bluetoothShieldEnabled", false));
        myView.faceGestureFlip.setChecked(getChecked("faceGestureFlipEnabled", false));
        myView.focalFlip.setChecked(getChecked("focalFlipEnabled", false));
        myView.sectionsViewMode.setChecked(getChecked("sectionsViewMode", false));
        myView.focalSnapInstant.setChecked(getChecked("focalSnapInstant", false));
        myView.sleekLook.setChecked(getChecked("sleekLook", false));

        if (getContext() != null) {
            ExposedDropDownArrayAdapter focalColorAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.focalTrackerColorDropdown, R.layout.view_exposed_dropdown_item, focalColorNames);
            myView.focalTrackerColorDropdown.setAdapter(focalColorAdapter);
        }
        myView.focalTrackerColorDropdown.setText(mainActivityInterface.getPreferences().getMyPreferenceString("focalTrackerColorName", "Pink"));

        ExposedDropDownArrayAdapter dyslexiaFontAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.dyslexiaFontDropdown, R.layout.view_exposed_dropdown_item, mainActivityInterface.getMyFonts().getFontsFromGoogle());
        myView.dyslexiaFontDropdown.setAdapter(dyslexiaFontAdapter);
        myView.dyslexiaFontDropdown.setText(mainActivityInterface.getPreferences().getMyPreferenceString("dyslexiaFont", "Lato"));

        // Cryo-Prompter
        myView.cryoPrompter.setChecked(getChecked("cryoPrompterEnabled", false));
        float prompterScale = mainActivityInterface.getPreferences().getMyPreferenceFloat("cryoPrompterScale", 1.4f);
        myView.activeLineScaleSlider.setValue(prompterScale);
        myView.activeLineScaleLabel.setText(getString(R.string.active_line_scale) + ": " + prompterScale + "x");

        if (getContext() != null) {
            ExposedDropDownArrayAdapter activeColorAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.activeLineColorDropdown, R.layout.view_exposed_dropdown_item, prompterColorNames);
            myView.activeLineColorDropdown.setAdapter(activeColorAdapter);
            myView.activeLineColorDropdown.setText(mainActivityInterface.getPreferences().getMyPreferenceString("cryoPrompterActiveColorName", "Gold"));

            ExposedDropDownArrayAdapter nextColorAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.nextLineColorDropdown, R.layout.view_exposed_dropdown_item, prompterColorNames);
            myView.nextLineColorDropdown.setAdapter(nextColorAdapter);
            myView.nextLineColorDropdown.setText(mainActivityInterface.getPreferences().getMyPreferenceString("cryoPrompterNextColorName", "Cyan"));

            // Cryo-Flow
            myView.cryoFlowEnable.setChecked(getChecked("cryoFlowEnabled", false));
            myView.cryoFlowIntensity.setValue(mainActivityInterface.getPreferences().getMyPreferenceFloat("cryoFlowIntensity", 1.0f));
            
            String[] flowPatterns = getString(R.string.cryo_flow_patterns_array).split(",");
            ExposedDropDownArrayAdapter flowPatternAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.cryoFlowPattern, R.layout.view_exposed_dropdown_item, flowPatterns);
            myView.cryoFlowPattern.setAdapter(flowPatternAdapter);
            myView.cryoFlowPattern.setText(mainActivityInterface.getPreferences().getMyPreferenceString("cryoFlowPatternName", "Tide"));

            // --- Face Gesture Mappings ---
            setupFaceGestureDropdowns();
        }
    }

    private void setupFaceGestureDropdowns() {
        if (getContext() == null) return;
        
        // Grab the full action list from PedalActions
        ArrayList<String> pedalActionsList = mainActivityInterface.getPedalActions().getActions();
        ExposedDropDownArrayAdapter adapter = new ExposedDropDownArrayAdapter(getContext(), R.layout.view_exposed_dropdown_item, pedalActionsList);
        
        // Helper to setup a single gesture dropdown
        setupSingleGestureDropdown(myView.gestureMouthOpenDropdown, adapter, "gestureMouthOpenAction", "next");
        setupSingleGestureDropdown(myView.gestureHeadNodDropdown, adapter, "gestureHeadNodAction", "down");
        setupSingleGestureDropdown(myView.gestureHeadRaiseDropdown, adapter, "gestureHeadRaiseAction", "up");
        setupSingleGestureDropdown(myView.gestureEyebrowRaiseDropdown, adapter, "gestureEyebrowRaiseAction", "");
        setupSingleGestureDropdown(myView.gestureWinkLeftDropdown, adapter, "gestureWinkLeftAction", "");
        setupSingleGestureDropdown(myView.gestureWinkRightDropdown, adapter, "gestureWinkRightAction", "");
        
        // Toggle visibility based on faceGestureFlipEnabled
        visibilityByBoolean(myView.faceGestureDetails, myView.faceGestureFlip.getChecked());
    }

    private void setupSingleGestureDropdown(com.garethevans.church.opensongtablet.customviews.ExposedDropDown dropdown, ExposedDropDownArrayAdapter adapter, String prefKey, String defaultValue) {
        dropdown.setAdapter(adapter);
        String currentActionCode = mainActivityInterface.getPreferences().getMyPreferenceString(prefKey, defaultValue);
        dropdown.setText(getActionFromActionCode(currentActionCode));
        
        dropdown.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String actionName = s.toString();
                String actionCode = getActionCodeFromAction(actionName);
                mainActivityInterface.getPreferences().setMyPreferenceString(prefKey, actionCode);
            }
        });
    }

    private String getActionCodeFromAction(String actionName) {
        ArrayList<String> actions = mainActivityInterface.getPedalActions().getActions();
        ArrayList<String> codes = mainActivityInterface.getPedalActions().getActionCodes();
        int index = actions.indexOf(actionName);
        if (index >= 0 && index < codes.size()) {
            return codes.get(index);
        }
        return "";
    }

    private String getActionFromActionCode(String actionCode) {
        ArrayList<String> actions = mainActivityInterface.getPedalActions().getActions();
        ArrayList<String> codes = mainActivityInterface.getPedalActions().getActionCodes();
        int index = codes.indexOf(actionCode);
        if (index >= 0 && index < actions.size()) {
            return actions.get(index);
        }
        return "";
    }

    private int getBracketValueFromString(String string) {
        int value = 0;
        for (int x=0; x<bracketStyles_Names.length; x++) {
            if (bracketStyles_Names[x].equals(string)) {
                value = bracketStyles_Ints[x];
            }
        }
        return value;
    }

    private String getBracketStringFromValue(int value) {
        String string = format_text_normal_string;
        for (int x=0; x<bracketStyles_Ints.length; x++) {
            if (bracketStyles_Ints[x]==value) {
                string = bracketStyles_Names[x];
            }
        }
        return string;
    }

    private boolean getChecked(String prefName, boolean fallback) {
        return mainActivityInterface.getPreferences().getMyPreferenceBoolean(prefName,fallback);
    }
    private void visibilityByBoolean(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
    private void sliderValToText(float value) {
        String hint = ((int)value) + "%";
        myView.trimLineSpacingSlider.setHint(hint);
    }
    private void setListeners() {
        // The switches
        myView.songSheet.setOnCheckedChangeListener((buttonView, isChecked) -> updateBooleanPreference("songSheet",isChecked,null));
        myView.prevInSet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("prevInSet",isChecked,null);
            mainActivityInterface.getDisplayPrevNext().updateShow();
        });
        myView.nextInSet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("nextInSet",isChecked,null);
            mainActivityInterface.getDisplayPrevNext().updateShow();
        });
        myView.prevNextSongMenu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("prevNextSongMenu", isChecked, null);
            mainActivityInterface.getDisplayPrevNext().updateShow();
        });
        myView.prevNextTextButtons.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("prevNextTextButtons", isChecked, null);
            mainActivityInterface.getDisplayPrevNext().updateShow();
        });
        myView.prevNextHide.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("prevNextHide", isChecked, null);
            mainActivityInterface.getDisplayPrevNext().updateShow();
        });
        myView.onscreenAutoscrollHide.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("onscreenAutoscrollHide", isChecked, null);
            mainActivityInterface.updateOnScreenInfo("setpreferences");
        });
        myView.onscreenCapoHide.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("onscreenCapoHide", isChecked, null);
            mainActivityInterface.updateOnScreenInfo("setpreferences");
        });
        myView.onscreenPadHide.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("onscreenPadHide", isChecked, null);
            mainActivityInterface.updateOnScreenInfo("setpreferences");
        });
        myView.boldChordsHeadings.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("displayBoldChordsHeadings",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
            displayInterface.updateDisplay("setSongContentPrefs");
        });
        myView.boldChorus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("displayBoldChorus", isChecked, null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
            displayInterface.updateDisplay("setSongContentPrefs");
        });
        myView.showChords.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("displayChords",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });
        myView.showLyrics.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("displayLyrics",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });
        myView.dyslexiaMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("dyslexiaMode", isChecked, null);
            mainActivityInterface.getMyThemeColors().setDyslexiaModeEnabled(isChecked);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
            displayInterface.updateDisplay("setSongContentPrefs");
        });
        myView.highContrastLyrics.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("highContrastLyrics", isChecked, null);
            mainActivityInterface.getMyThemeColors().setHighContrastLyricsEnabled(isChecked);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
            displayInterface.updateDisplay("setSongContentPrefs");
        });
        myView.voiceControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("voiceControlEnabled", isChecked, null);
            if (getActivity() instanceof com.garethevans.church.opensongtablet.MainActivity) {
                ((com.garethevans.church.opensongtablet.MainActivity) getActivity()).toggleVoiceControl(isChecked);
            }
        });
        myView.jsxCalibrate.setOnClickListener(v -> {
            if (getActivity() instanceof com.garethevans.church.opensongtablet.MainActivity) {
                ((com.garethevans.church.opensongtablet.MainActivity) getActivity()).startJsxCalibration();
            }
        });
        myView.jsxMasterThreshold.addOnChangeListener((slider, value, fromUser) -> {
            myView.jsxMasterThresholdLabel.setText("JSx Master Threshold: " + (int)value + " dB");
        });
        myView.jsxMasterThreshold.addOnSliderTouchListener(new com.google.android.material.slider.Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull com.google.android.material.slider.Slider slider) {}

            @Override
            public void onStopTrackingTouch(@NonNull com.google.android.material.slider.Slider slider) {
                float val = slider.getValue();
                mainActivityInterface.getPreferences().setMyPreferenceFloat("jsxMasterThreshold", val);
                if (getActivity() instanceof com.garethevans.church.opensongtablet.MainActivity) {
                    ((com.garethevans.church.opensongtablet.MainActivity) getActivity()).setJsxMasterThreshold(val);
                }
            }
        });
        myView.presoOrder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("usePresentationOrder",isChecked,null);
            mainActivityInterface.getPresenterSettings().setUsePresentationOrder(isChecked);
        });
        myView.keepMultiline.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            updateBooleanPreference("multiLineVerseKeepCompact",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        }));
        myView.trimSections.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("trimSections",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });
        myView.addSectionSpace.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("addSectionSpace",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });
        myView.trimLineSpacing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("trimLines",isChecked,myView.trimLineSpacingSlider);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });
        myView.trimWordSpacing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("trimWordSpacing",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });
        myView.bracketsStyle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int value = getBracketValueFromString(myView.bracketsStyle.getText().toString());
                mainActivityInterface.getPreferences().setMyPreferenceInt("bracketsStyle",value);
                mainActivityInterface.getProcessSong().updateProcessingPreferences();
            }
        });
        myView.curlyBrackets.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            updateBooleanPreference("curlyBrackets",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
            displayInterface.updateDisplay("setSongContentPrefs");
        }));
        myView.curlyBracketsDevice.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            updateBooleanPreference("curlyBracketsDevice",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
            displayInterface.updateDisplay("setSongContentPrefs");
        }));
        // TODO Maybe add later?
        /*myView.addSectionBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("addSectionBox",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });*/
        myView.filterSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("filterSections",isChecked,myView.filterLayout);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });
        myView.filterShow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("filterShow",isChecked,null);
            mainActivityInterface.getProcessSong().updateProcessingPreferences();
        });

        // The slider
        myView.trimLineSpacingSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) { }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                // Save the new value
                float percentage = slider.getValue()/100f;
                mainActivityInterface.getPreferences().setMyPreferenceFloat("lineSpacing",percentage);
                mainActivityInterface.getProcessSong().updateProcessingPreferences();
            }
        });
        myView.trimLineSpacingSlider.addOnChangeListener((slider, value, fromUser) -> sliderValToText(value));

        // The button
        myView.filterSave.setOnClickListener(v -> {
            // Get the text from the edittext
            Log.d(TAG,"myView.filters.getText():"+myView.filters.getText());
            if (myView.filters.getText()!=null) {
                String s = myView.filters.getText().toString();
                // Split by line in order to trim
                String[] lines = s.split("\n");
                StringBuilder stringBuilder = new StringBuilder();
                for (String line:lines) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        stringBuilder.append(line).append("\n");
                    }
                }
                String newText = stringBuilder.toString().trim();
                // Put the corrected text back in
                myView.filters.setText(newText);
                // Save it
                mainActivityInterface.getPreferences().setMyPreferenceString("filterText",newText);
            }
        });
        myView.pdfHorizontal.setOnCheckedChangeListener((compoundButton, b) -> mainActivityInterface.getPreferences().setMyPreferenceBoolean("pdfLandscapeView", b));

        myView.bluetoothShield.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("bluetoothShieldEnabled", isChecked, null);
            if (getActivity() instanceof com.garethevans.church.opensongtablet.MainActivity) {
                // We'll add this method to MainActivity later or trigger it here
                toggleBluetoothShield(isChecked);
            }
        });

        myView.faceGestureFlip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("faceGestureFlipEnabled", isChecked, myView.faceGestureDetails);
            // Trigger processor update if needed
        });

        myView.focalFlip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("focalFlipEnabled", isChecked, null);
            displayInterface.updateDisplay("setSongContentPrefs");
        });

        myView.sectionsViewMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("sectionsViewMode", isChecked, null);
            displayInterface.updateDisplay("setSongContentPrefs");
        });

        myView.focalSnapInstant.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("focalSnapInstant", isChecked, null);
        });

        myView.sleekLook.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBooleanPreference("sleekLook", isChecked, null);
            displayInterface.updateDisplay("setSongContentPrefs");
            // Also notify ThemeColors if necessary, or let fragments check it during their refresh
        });

        myView.focalTrackerColorDropdown.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String selected = s.toString();
                mainActivityInterface.getPreferences().setMyPreferenceString("focalTrackerColorName", selected);
                if (selected.equals(focal_tracker_custom_string)) {
                    showColorPicker();
                } else {
                    // Save standard color
                    saveStandardFocalColor(selected);
                }
                displayInterface.updateDisplay("setSongContentPrefs");
            }
        });

        myView.dyslexiaFontDropdown.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String selected = s.toString();
                mainActivityInterface.getPreferences().setMyPreferenceString("dyslexiaFont", selected);
                mainActivityInterface.getMyFonts().changeFont("fontDyslexia", selected, mainActivityInterface.getMainHandler());
                mainActivityInterface.getProcessSong().updateProcessingPreferences();
                displayInterface.updateDisplay("setSongContentPrefs");
            }
        });

        myView.cryoFlowPattern.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String patternName = s.toString();
                mainActivityInterface.getPreferences().setMyPreferenceString("cryoFlowPatternName", patternName);
                String[] flowPatterns = getString(R.string.cryo_flow_patterns_array).split(",");
                int index = 0;
                for (int i=0; i<flowPatterns.length; i++) {
                    if (flowPatterns[i].equals(patternName)) index = i;
                }
                mainActivityInterface.getPreferences().setMyPreferenceInt("cryoFlowPatternIndex", index);
                displayInterface.updateDisplay("setSongContentPrefs");
            }
        });

        myView.cryoFlowIntensity.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override public void onStartTrackingTouch(@NonNull Slider slider) {}
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                mainActivityInterface.getPreferences().setMyPreferenceFloat("cryoFlowIntensity", slider.getValue());
                displayInterface.updateDisplay("setSongContentPrefs");
            }
        });

        myView.cryoFlowPrimaryColorBtn.setOnClickListener(v -> {
            ChooseColorBottomSheet chooseColorBottomSheet = new ChooseColorBottomSheet(this, TAG, "cryoFlowPrimaryColor");
            chooseColorBottomSheet.show(getParentFragmentManager(), "flow_primary_picker");
        });

        myView.cryoFlowSecondaryColorBtn.setOnClickListener(v -> {
            ChooseColorBottomSheet chooseColorBottomSheet = new ChooseColorBottomSheet(this, TAG, "cryoFlowSecondaryColor");
            chooseColorBottomSheet.show(getParentFragmentManager(), "flow_secondary_picker");
        });
    }

    private void toggleBluetoothShield(boolean enable) {
        android.content.Intent intent = new android.content.Intent(getContext(), com.garethevans.church.opensongtablet.bluetooth.BluetoothShieldService.class);
        if (enable) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                getContext().startForegroundService(intent);
            } else {
                getContext().startService(intent);
            }
        } else {
            getContext().stopService(intent);
        }
    }

    private void saveStandardFocalColor(String name) {
        int color = android.graphics.Color.parseColor("#FF4081"); // Pink default
        if (name.equals("Amber")) color = android.graphics.Color.parseColor("#FFC107");
        else if (name.equals("Cyan")) color = android.graphics.Color.parseColor("#00BCD4");
        mainActivityInterface.getPreferences().setMyPreferenceInt("focalTrackerColor", color);
    }

    private void showColorPicker() {
        ChooseColorBottomSheet chooseColorBottomSheet = new ChooseColorBottomSheet(this, TAG, "focalTrackerColor");
        chooseColorBottomSheet.show(getParentFragmentManager(), "focal_color_picker");
    }

    private int parseColorFromName(String name) {
        switch (name) {
            case "Gold": return android.graphics.Color.parseColor("#FFD700");
            case "Cyan": return android.graphics.Color.parseColor("#00FFFF");
            case "Slate": return android.graphics.Color.parseColor("#708090");
            case "White": return android.graphics.Color.WHITE;
            case "Green": return android.graphics.Color.GREEN;
            case "Pink": return android.graphics.Color.parseColor("#FF4081");
            case "Amber": return android.graphics.Color.parseColor("#FFC107");
            default: return android.graphics.Color.WHITE;
        }
    }

    private void updateBooleanPreference(String prefName, boolean isChecked, View viewToShowHide) {
        mainActivityInterface.getPreferences().setMyPreferenceBoolean(prefName,isChecked);
        mainActivityInterface.getProcessSong().updateProcessingPreferences();
        if (viewToShowHide!=null) {
            visibilityByBoolean(viewToShowHide,isChecked);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myView = null;
    }
}
