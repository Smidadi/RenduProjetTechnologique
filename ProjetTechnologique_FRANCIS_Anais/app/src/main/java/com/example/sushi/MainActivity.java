package com.example.sushi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.Random;


import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creation de l'instance image

        BitmapFactory.Options options = new BitmapFactory.Options(); // permet la manipulation de tous les pixels
        options.inMutable = true;
        final Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.paysage, options);

        ImageView iv = findViewById(R.id.projectImg);
        iv.setImageBitmap(img); // permet de modifier l'image

        // Correspond aux images pour les histogrammes de l'image

        ImageView histoR = findViewById(R.id.histoR);
        ImageView histoG = findViewById(R.id.histoG);
        ImageView histoB = findViewById(R.id.histoB);

        final Bitmap hR = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);
        final Bitmap hG = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);
        final Bitmap hB = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);

        histoR.setImageBitmap(hR);
        histoG.setImageBitmap(hG);
        histoB.setImageBitmap(hB);

        // Correspond à la taille de l'image principale

        final int w = img.getWidth();
        final int h = img.getHeight();

        TextView tv = findViewById(R.id.sizeImg); // affiche la taille de l'image (cherche l'id)
        tv.setText("Taille d'origine "+ w + "*" + h);

        final int[] mon_image = new int[w * h];
        img.getPixels(mon_image, 0, w, 0, 0, w, h);

        // Dessine les histogrammes de l'image principale

        JavaFilter.dessinHisto(img, hR, hG, hB);

        // Correspond au menu déroulant de l'application

        final Button menuBtn = findViewById(R.id.menu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, menuBtn);

                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        ImageView iv = findViewById(R.id.projectImg); // Trouve l'image à modifier
                        // Afficher l'image d'origine
                        if (item.getTitle().equals("ORIGINAL")) {
                            img.setPixels(mon_image, 0, w, 0, 0, w, h);
                            JavaFilter.dessinHisto(img, hR, hG, hB);
                        }
                        // Appelle la fonction qui grise l'image
                        if (item.getTitle().equals("GREY ME")) {
                            toGrayRS(img);
                            //JavaFilter.toGrey(img);
                            //countHistogramRS(img);
                            //histogramRS(img);
                            JavaFilter.dessinHisto(img, hR, hG, hB);
                        }
                        // Appelle la fonction qui met une couleur aléatoire à l'image
                        if (item.getTitle().equals("RAND ME")) {
                            Random r = new Random();
                            float hue = r.nextFloat() * 360; // trouve un nombre random pour le côté RenderScript
                            colorizeRS(img, hue);
                            JavaFilter.dessinHisto(img, hR, hG, hB);
                        }
                        // Appelle la fonction qui isole la couleur rouge de l'image
                        if (item.getTitle().equals("JUST RED")) {
                            onecolorRS(img);
                            JavaFilter.dessinHisto(img, hR, hG, hB);
                        }
                        // Appelle la fonction qui fait le contraste de l'image
                        if (item.getTitle().equals("CONTRASTE")) {
                            JavaFilter.contraste(img);
                            JavaFilter.dessinHisto(img, hR, hG, hB);
                        }
                        iv.setImageBitmap(img); // Actualise l'image
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    // toGrayRS : grise l'image à partir de RenderScript
    void toGrayRS(Bitmap  bmp) {
        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_gray grayScript = new ScriptC_gray(rs);

        grayScript.forEach_toGray(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }

    // colorizeRS : colore aléatoirement l'image à partir de RenderScript
    void colorizeRS(Bitmap  bmp, float hue) {
        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs);
        colorizeScript.set_hue(hue);

        colorizeScript.forEach_colorize(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();
    }

    // onecolorRS : isole le rouge de l'image à partir de RenderScript
    void onecolorRS(Bitmap  bmp) {
        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_onecolor onecolorScript = new ScriptC_onecolor(rs);

        onecolorScript.forEach_onecolor(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        onecolorScript.destroy();
        rs.destroy();
    }

    // histogramRS : crée les histogrammes à partir de RenderScript (non fonctionnel)
    Bitmap histogramRS(Bitmap  bmp) {
            //Get image size
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        //Create new bitmap
        Bitmap res = bmp.copy(bmp.getConfig(), true);

        //Create renderscript
        RenderScript rs = RenderScript.create(this);

        //Create allocation from Bitmap
        Allocation allocationA = Allocation.createFromBitmap(rs, res);

        //Create allocation with same type
        Allocation allocationB = Allocation.createTyped(rs, allocationA.getType());

        //Create script from rs file.
        ScriptC_histogram histEqScript = new ScriptC_histogram(rs);

        //Set size in script
        histEqScript.set_size(width*height);

        //Call the first kernel.
        histEqScript.forEach_root(allocationA, allocationB);

        //Call the rs method to compute the remap array
        histEqScript.invoke_createRemapArray();

        //Call the second kernel
        histEqScript.forEach_remaptoRGB(allocationB, allocationA);

        //Copy script result into bitmap
        allocationA.copyTo(res);

        //Destroy everything to free memory
        allocationA.destroy();
        allocationB.destroy();
        histEqScript.destroy();
        rs.destroy();

        return res;

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}