package com.garethevans.church.opensongtablet.screensetup;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garethevans.church.opensongtablet.R;
import com.garethevans.church.opensongtablet.customviews.ExposedDropDownArrayAdapter;
import com.garethevans.church.opensongtablet.databinding.SettingsDyslexaSuiteBinding;
import com.garethevans.church.opensongtablet.interfaces.DisplayInterface;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;

import java.util.ArrayList;
import java.util.Arrays;

public class DyslexaSettingsFragment extends Fragment {

    private SettingsDyslexaSuiteBinding myView;
    private MainActivityInterface mainActivityInterface;
    private DisplayInterface displayInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivityInterface = (MainActivityInterface) context;
        displayInterface = (DisplayInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = SettingsDyslexaSuiteBinding.inflate(inflater, container, false);
        return myView.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        // Prompter
        boolean prompterEnabled = mainActivityInterface.getPreferences().getMyPreferenceBoolean("dyslexaPrompterEnabled", false);
        myView.dyslexaPrompterEnable.setChecked(prompterEnabled);
        myView.dyslexaPrompterEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mainActivityInterface.getPreferences().setMyPreferenceBoolean("dyslexaPrompterEnabled", isChecked);
            displayInterface.updateDisplay("setSongContentPrefs");
        });

        float prompterScale = mainActivityInterface.getPreferences().getMyPreferenceFloat("dyslexaPrompterScale", 1.2f);
        myView.prompterScaleSlider.setValue(prompterScale);
        myView.prompterScaleLabel.setText(getString(R.string.active_line_scale) + ": " + prompterScale + "x");
        myView.prompterScaleSlider.addOnChangeListener((slider, value, fromUser) -> {
            myView.prompterScaleLabel.setText(getString(R.string.active_line_scale) + ": " + value + "x");
            mainActivityInterface.getPreferences().setMyPreferenceFloat("dyslexaPrompterScale", value);
            displayInterface.updateDisplay("setSongContentPrefs");
        });

        // Flow
        boolean flowEnabled = mainActivityInterface.getPreferences().getMyPreferenceBoolean("dyslexaFlowEnabled", false);
        myView.dyslexaFlowEnable.setChecked(flowEnabled);
        myView.dyslexaFlowEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mainActivityInterface.getPreferences().setMyPreferenceBoolean("dyslexaFlowEnabled", isChecked);
            displayInterface.updateDisplay("setSongContentPrefs");
        });

        String[] patterns = getString(R.string.dyslexa_flow_patterns_array).split(",");
        ExposedDropDownArrayAdapter patternAdapter = new ExposedDropDownArrayAdapter(getContext(), myView.dyslexaFlowPattern, R.layout.view_exposed_dropdown_item, new ArrayList<>(Arrays.asList(patterns)));
        myView.dyslexaFlowPattern.setAdapter(patternAdapter);
        String currentPattern = mainActivityInterface.getPreferences().getMyPreferenceString("dyslexaFlowPatternName", patterns[0]);
        myView.dyslexaFlowPattern.setText(currentPattern, false);
        myView.dyslexaFlowPattern.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                mainActivityInterface.getPreferences().setMyPreferenceString("dyslexaFlowPatternName", s.toString());
                displayInterface.updateDisplay("setSongContentPrefs");
            }
        });

        myView.flowPrimaryColor.setColor(mainActivityInterface.getPreferences().getMyPreferenceInt("dyslexaFlowColorPrimary", android.graphics.Color.BLUE));
        myView.flowSecondaryColor.setColor(mainActivityInterface.getPreferences().getMyPreferenceInt("dyslexaFlowColorSecondary", android.graphics.Color.CYAN));
        
        myView.flowPrimaryColor.setOnClickListener(v -> chooseColor("dyslexaFlowColorPrimary"));
        myView.flowSecondaryColor.setOnClickListener(v -> chooseColor("dyslexaFlowColorSecondary"));

        // Face Gestures
        boolean faceEnabled = mainActivityInterface.getPreferences().getMyPreferenceBoolean("faceGestureFlipEnabled", false);
        myView.faceGestureEnable.setChecked(faceEnabled);
        myView.faceGestureDetails.setVisibility(faceEnabled ? View.VISIBLE : View.GONE);
        myView.faceGestureEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mainActivityInterface.getPreferences().setMyPreferenceBoolean("faceGestureFlipEnabled", isChecked);
            myView.faceGestureDetails.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        setupGestureDropdowns();
        
        applyPalette();
    }

    private void setupGestureDropdowns() {
        ArrayList<String> actionList = mainActivityInterface.getPedalActions().getActions();

        setupOneGesture("gestureMouthOpen", myView.gestureMouthOpen, actionList);
        setupOneGesture("gestureNod", myView.gestureNod, actionList);
        setupOneGesture("gestureRaise", myView.gestureRaise, actionList);
        setupOneGesture("gestureEyebrows", myView.gestureEyebrows, actionList);
        setupOneGesture("gestureWinkLeft", myView.gestureWinkLeft, actionList);
        setupOneGesture("gestureWinkRight", myView.gestureWinkRight, actionList);
    }

    private void setupOneGesture(String key, com.garethevans.church.opensongtablet.customviews.ExposedDropDown dropDown, ArrayList<String> actions) {
        ExposedDropDownArrayAdapter adapter = new ExposedDropDownArrayAdapter(getContext(), dropDown, R.layout.view_exposed_dropdown_item, actions);
        dropDown.setAdapter(adapter);
        String current = mainActivityInterface.getPreferences().getMyPreferenceString(key, actions.get(0));
        dropDown.setText(current, false);
        dropDown.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                mainActivityInterface.getPreferences().setMyPreferenceString(key, s.toString());
            }
        });
        dropDown.setPalette(mainActivityInterface.getPalette());
    }

    private void chooseColor(String key) {
        ChooseColorBottomSheet bottomSheet = new ChooseColorBottomSheet(this, "dyslexaSettingsFragment", key);
        bottomSheet.show(mainActivityInterface.getMyFragmentManager(), "ChooseColorBottomSheet");
    }

    private void applyPalette() {
        myView.getRoot().setBackgroundColor(mainActivityInterface.getPalette().background);
        myView.dyslexaPrompterEnable.setPalette(mainActivityInterface.getPalette());
        myView.prompterScaleSlider.setPalette(mainActivityInterface.getPalette());
        myView.dyslexaFlowEnable.setPalette(mainActivityInterface.getPalette());
        myView.dyslexaFlowPattern.setPalette(mainActivityInterface.getPalette());
        myView.flowPrimaryColor.setPalette(mainActivityInterface.getPalette());
        myView.flowSecondaryColor.setPalette(mainActivityInterface.getPalette());
        myView.faceGestureEnable.setPalette(mainActivityInterface.getPalette());
        // configureGesturesBtn is handled by the dropdowns themselves
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myView = null;
    }
}
