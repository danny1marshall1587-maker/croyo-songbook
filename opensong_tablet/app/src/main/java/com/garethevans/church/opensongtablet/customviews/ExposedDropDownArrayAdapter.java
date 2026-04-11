package com.garethevans.church.opensongtablet.customviews;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.garethevans.church.opensongtablet.R;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;

import java.util.ArrayList;

public class ExposedDropDownArrayAdapter extends ArrayAdapter<String> {

    @SuppressWarnings({"unused","FieldCanBeLocal"})
    private final String TAG = "ExposedDropDownAdapter";
    private MainActivityInterface mainActivityInterface;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean showFontPreview = false;

    public ExposedDropDownArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        // Because we have not passed in a reference to the exposed dropdown,
        // we can't show the preview.
        super(context, resource, objects);
        commonInit(context);
    }

    public ExposedDropDownArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        commonInit(context);
    }

    public ExposedDropDownArrayAdapter(@NonNull Context context, ExposedDropDown exposedDropDown, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        commonInit(context);
    }

    public ExposedDropDownArrayAdapter(@NonNull Context context, ExposedDropDown exposedDropDown, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        commonInit(context);
    }

    private void commonInit(Context context) {
        if (context instanceof MainActivityInterface) {
            mainActivityInterface = (MainActivityInterface) context;
        }
    }

    /**
     * Enable or disable font previews (with "The quick brown fox..." text and actual typeface)
     */
    public void setShowFontPreview(boolean showFontPreview) {
        this.showFontPreview = showFontPreview;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        MyMaterialSimpleTextView text = view.findViewById(R.id.popupText);
        
        if (mainActivityInterface != null) {
            text.setTextColor(mainActivityInterface.getPalette().textColor); // text color for dropdown items

            String item = getItem(position);
            if (item != null && showFontPreview) {
                String displayName = item;
                if (item.startsWith("Fonts/")) displayName = item.replace("Fonts/", "");
                if (item.startsWith("Vault/")) displayName = item.replace("Vault/", "");

                text.setText(displayName + " - The quick brown fox...");

                // Check if it's likely a font choice (FontSetupFragment passes font names here)
                // We use the new getTypeface helper to apply the font to the view
                mainActivityInterface.getMyFonts().getTypeface(item, text, handler);
            }
        }

        return view;
    }
}
