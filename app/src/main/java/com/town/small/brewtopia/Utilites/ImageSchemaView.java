package com.town.small.brewtopia.Utilites;

/**
 * Created by Andrew on 10/17/2016.
 */
import android.content.Context;
import android.widget.ImageView;

import com.town.small.brewtopia.DataClass.BrewImageSchema;

import java.io.Serializable;

public class ImageSchemaView extends ImageView
        implements Serializable {

    BrewImageSchema schema;

    public ImageSchemaView(Context context, BrewImageSchema aSchema) {
        super(context);
        schema = aSchema;
    }

    public BrewImageSchema getSchema() {
        return schema;
    }
}
