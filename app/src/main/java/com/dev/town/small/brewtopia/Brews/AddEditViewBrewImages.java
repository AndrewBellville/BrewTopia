package com.dev.town.small.brewtopia.Brews;

/**
 * Created by Andrew on 10/17/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.BrewImageSchema;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.Utilites.ImageSchemaView;

import java.util.Iterator;
import java.util.List;

public class AddEditViewBrewImages extends Fragment {

    // Log cat tag
    private static final String LOG = "UserImages";

    private static int RESULT_LOAD_IMAGE = 1;
    private static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private DataBaseManager dbManager;

    private TableLayout imageTable;
    private Button importImageButton;
    private Button openCameraButton;
    private RadioButton deleteImage;
    private TextView noImage;
    private boolean isDelete = false;

    BrewSchema brewSchema;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_brew_images,container,false);
        Log.e(LOG, "Entering: onCreate");

        imageTable = (TableLayout)view.findViewById(R.id.ImageTable);
        noImage = (TextView)view.findViewById(R.id.NoImage);

        importImageButton = (Button)view.findViewById(R.id.ImportImageButton);
        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImportImageClick(view);
            }
        });
        openCameraButton = (Button)view.findViewById(R.id.OpenCameraButton);
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePictureClick(view);
            }
        });
        deleteImage = (RadioButton)view.findViewById(R.id.DeleteRadioButton);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });
        dbManager = DataBaseManager.getInstance(getActivity());

        brewSchema = BrewActivityData.getInstance().getAddEditViewBrew();

        //Hide Button if We cant edit / if we are adding a new brew
        if(!BrewActivityData.getInstance().CanEdit() || BrewActivityData.getInstance().getAddEditViewState() == BrewActivityData.DisplayMode.ADD) {
            importImageButton.setVisibility(View.INVISIBLE);
            openCameraButton.setVisibility(View.INVISIBLE);
            deleteImage.setVisibility(View.INVISIBLE);
        }

        LoadBrewImages();

        return view;
    }

    //TODO Optimize
    //TODO Load only new images based on creation date
    private void LoadBrewImages() {
        Log.e(LOG, "Entering: LoadBrewImages");

        //set all sizing
        Display display =  getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int imageSize = 300;
        int imagePadding = 5;
        int totalImagesPerRow = width / (imageSize + imagePadding);
        int imagesInRowCount = 0;

        List<BrewImageSchema> imageList = BrewActivityData.getInstance().getAddEditViewBrew().getBrewImageSchemaList();
        imageTable.removeAllViews();
        // just for testing
        if (imageList.size() > 0) {
            ToggleNoImageDisp(false);
            TableRow row = new TableRow(getActivity());
            for (Iterator<BrewImageSchema> i = imageList.iterator(); i.hasNext(); ) {

                //get the next image schema
                BrewImageSchema imageSchema = i.next();

                //create image view
                View v = new ImageView(getActivity());
                ImageSchemaView image = new ImageSchemaView(v.getContext(), imageSchema);
                image.setClickable(true);
                image.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View aView) {
                        ImageClickHandler(aView);
                    }
                });

                image.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);


                Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageSchema.getImage(), imageSize, imageSize, true);

                //add image schema to image
                image.setImageBitmap(scaledBitmap);

                row.addView(image);
                imagesInRowCount++;

                // if count is total that can fit or is the last element
                if (totalImagesPerRow == imagesInRowCount || !i.hasNext()) {
                    imageTable.addView(row);
                    row = new TableRow(getActivity());
                    imagesInRowCount = 0;
                }
            }
        } else {
            ToggleNoImageDisp(true);
        }

        imageTable.invalidate();
    }

    // handles the button click and opens Image gallery
    public void ImportImageClick(View aView) {
        Log.e(LOG, "Entering: ImportImageClick");

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, RESULT_LOAD_IMAGE);

    }

    // handles the button click and intent for camera
    public void TakePictureClick(View aView) {
        Log.e(LOG, "Entering: TakePictureClick");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // handles the image click
    public void ImageClickHandler(View aView) {
        Log.e(LOG, "Entering: ImageClickHandler");
        //get image that was clicked
        ImageSchemaView temp = (ImageSchemaView) aView;
        if (isDelete) {
            dbManager.deleteBrewImageById(temp.getSchema().getImageId());
            //call to reload images
            resetBrewData(brewSchema.getBrewId());

        } else {
            //Create and intent which will open ImageDisplay for image clicked
            Intent intent = new Intent(getActivity(), BrewImageView.class);
            //Set image to display in Brew Data
            BrewActivityData.getInstance().setImageDisplyBitmap(temp.getSchema().getImage());
            //start next activity
            startActivity(intent);
        }
    }

    // handles the selection of a single image from image gallery and stores to db
    @Override
    public void onActivityResult(int aRequestCode, int aResultCode, Intent aData) {
        Log.e(LOG, "Entering: onActivityResult requestCode [" + aRequestCode + "] resultCode[" + aResultCode + "]");
        super.onActivityResult(aRequestCode, aResultCode, aData);

        if(aRequestCode == RESULT_LOAD_IMAGE || aRequestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            if (aResultCode == Activity.RESULT_OK) {

                BrewImageSchema imageSchema = new BrewImageSchema();
                imageSchema.setBrewId(brewSchema.getBrewId());
                imageSchema.setImage(GetImageFromData(aData));
                dbManager.CreateBrewImage(imageSchema);
            }
            else if (aResultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        //call to reload images
        resetBrewData(brewSchema.getBrewId());
    }

    private Bitmap GetImageFromData(Intent aData) {
        Log.e(LOG, "Entering: GetImageFromData");
        Uri selectedImage = aData.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        //TODO should save this image but need to optimize
        Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

        Log.e(LOG, Integer.toString(yourSelectedImage.getWidth())+"  "+Integer.toString(yourSelectedImage.getHeight()));

        while(yourSelectedImage.getWidth() > 500 || yourSelectedImage.getHeight() > 500){
            yourSelectedImage = Bitmap.createScaledBitmap(yourSelectedImage, yourSelectedImage.getWidth()/2, yourSelectedImage.getHeight()/2, true);
        }

        return yourSelectedImage;
    }

    private void ToggleNoImageDisp(boolean aHasNoImage) {
        if (aHasNoImage)
            noImage.setVisibility(View.VISIBLE);
        else
            noImage.setVisibility(View.GONE);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        if(isDelete) ((RadioButton) view).setChecked(false);
        isDelete = ((RadioButton) view).isChecked();
    }

    private void resetBrewData(long aBrewId)
    {
        BrewActivityData.getInstance().setAddEditViewBrew(dbManager.getBrew(aBrewId));
        LoadBrewImages();
    }

}
