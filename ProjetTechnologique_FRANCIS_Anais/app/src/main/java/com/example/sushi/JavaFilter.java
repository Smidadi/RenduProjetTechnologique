package com.example.sushi;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;

public class JavaFilter {
    // toGrey : grise l'image
    static void toGrey(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        int[] pixel = new int[w * h];
        int i = 0;

        // Récupère tous les pixels de l'image
        bmp.getPixels(pixel, 0, w, 0, 0, w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = Color.red(pixel[i]);
                int g = Color.green(pixel[i]);
                int b = Color.blue(pixel[i]);

                // Calcul pour griser l'image
                int moy = (int) (0.3 * r + 0.59 * g + 0.11 * b);

                pixel[i] = Color.rgb(moy, moy, moy);
                i++;
            }
        }
        // Affecte tous les pixels du tableau pixel à l'image
        bmp.setPixels(pixel, 0, w, 0, 0, w, h);
    }

    // tabHisto : calcule les valeurs de rouge / vert / bleu pour l'histogramme
    static int[] tabHisto(int[] pixel, float[] histo, int w, int h, String color){
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < (h * histo[x]); y++) {
                int i = ((h * w) - w) - (y * w) + x;
                switch (color) {
                    case "red":
                        pixel[i] = Color.RED;
                        break;
                    case "green":
                        pixel[i] = Color.GREEN;
                        break;
                    case "blue":
                        pixel[i] = Color.BLUE;
                        break;
                }
            }
        }
        return pixel;
    }

    // normalise : normalise l'histogramme de l'image
    static float normalise(float[] histo){
        float max = 0;
        for(int i = 0; i < histo.length; i++){
            if(histo[i] > max){
                max = histo[i];
            }
        }
        return max;
    }

    // dessinHisto : dessine les trois histogrammes de l'image
    static void dessinHisto(Bitmap bmp, Bitmap hR, Bitmap hG, Bitmap hB){
        int w = hR.getWidth();
        int h = hR.getHeight();

        // Déclaration des tableaux en fonction de la taille des images pour les histogrammes
        int[] nb_hR = new int[hR.getWidth() * hR.getHeight()];
        int[] nb_hG = new int[hG.getWidth() * hG.getHeight()];
        int[] nb_hB = new int[hB.getWidth() * hB.getHeight()];

        // Calcule les valeurs rouge / vert / bleu de l'image
        float[] histoR = valueHisto(bmp, "red");
        float[] histoG = valueHisto(bmp, "green");
        float[] histoB = valueHisto(bmp, "blue");

        hR.getPixels(nb_hR, 0, hR.getWidth(), 0, 0, hR.getWidth(), hR.getHeight());
        hG.getPixels(nb_hG, 0, hG.getWidth(), 0, 0, hG.getWidth(), hG.getHeight());
        hB.getPixels(nb_hB, 0, hB.getWidth(), 0, 0, hB.getWidth(), hB.getHeight());

        // Normaliser l'histogramme
        float[] moyR = new float[256];
        float[] moyG = new float[256];
        float[] moyB = new float[256];
        float maxR = normalise(histoR);
        float maxG = normalise(histoG);
        float maxB = normalise(histoB);

        for(int i = 0; i < moyR.length; i++){
            moyR[i] = histoR[i] / maxR;
            moyG[i] = histoG[i] / maxG;
            moyB[i] = histoB[i] / maxB;
        }

        // Affecte les valeurs à l'histogramme
        nb_hR = tabHisto(nb_hR, moyR, w, h, "red");
        nb_hG = tabHisto(nb_hG, moyG, w, h, "green");
        nb_hB = tabHisto(nb_hB, moyB, w, h, "blue");

        hR.setPixels(nb_hR, 0, w, 0, 0, w, h);
        hG.setPixels(nb_hG, 0, hG.getWidth(), 0, 0, hG.getWidth(), hG.getHeight());
        hB.setPixels(nb_hB, 0, hB.getWidth(), 0, 0, hB.getWidth(), hB.getHeight());
    }

    // valueHisto : calcule les valeurs rouge / vert / bleu de l'iamge
    static float[] valueHisto(Bitmap bmp, String color) {    // 0 : red | 1 : blue | 2 : green
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        float[] histo = new float[256];

        for (int i = 0; i < histo.length; i++) {
            histo[i] = 0;
        }

        int[] pixel = new int[w * h];

        bmp.getPixels(pixel, 0, w, 0, 0, w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                int value = 0;
                switch (color) {
                    case "red":
                        value = Color.red(pixel[i]);
                        break;
                    case "blue":
                        value = Color.blue(pixel[i]);
                        break;
                    case "green":
                        value = Color.green(pixel[i]);
                        break;
                }
                histo[value]++;
            }
        }
        return histo;
    }

    // max : retourne le maximum en partant de la fin de l'image
    static int max(Bitmap bmp, String color) {
        float[] histo = valueHisto(bmp, color);
        for (int i = 255; i >= 0; i--) {
            if (histo[i] > 10) {
                return i;
            }
        }
        return 0;
    }

    // min : retourne le minimum en partant du début de l'image
    static int min(Bitmap bmp, String color) {
        float[] histo = valueHisto(bmp, color);
        for (int i = 0; i < histo.length; i++) {
            if (histo[i] > 10) {
                return i;
            }
        }
        return 0;
    }

    // contraste : augmente le contraste de l'image
    static void contraste(Bitmap bmp) {
        float[] LUTR = new float[256];
        float[] LUTG = new float[256];
        float[] LUTB = new float[256];

        int w = bmp.getWidth();
        int h = bmp.getHeight();

        int[] nb_pix = new int[w * h];

        bmp.getPixels(nb_pix, 0, w, 0, 0, w, h);

        int maxR = max(bmp, "red");  // INFO : inverser min et max donne un filtre négatif
        int minR = min(bmp, "red");


        int maxG = max(bmp, "green");
        int minG = min(bmp, "green");

        int maxB = max(bmp, "blue");
        int minB = min(bmp, "blue");

        for (int ng = 0; ng < 256; ng++) {
            LUTR[ng] = (255 * (ng - minR)) / (maxR - minR);
            LUTB[ng] = (255 * (ng - minB)) / (maxB - minB);
            LUTG[ng] = (255 * (ng - minG)) / (maxG - minG);
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                int r = (int) LUTR[Color.red(nb_pix[i])];
                int g = (int) LUTG[Color.green(nb_pix[i])];
                int b = (int) LUTB[Color.blue(nb_pix[i])];

                nb_pix[i] = Color.rgb(r, g, b);
            }
        }
        bmp.setPixels(nb_pix, 0, w, 0, 0, w, h);
    }

    // just_red : isole la couleur rouge de l'image
    static void just_red(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        int[] pixel = new int[w * h];
        int i = 0;

        bmp.getPixels(pixel, 0, w, 0, 0, w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = Color.red(pixel[i]);
                int g = Color.green(pixel[i]);
                int b = Color.blue(pixel[i]);


                float[] hsv = new float[3];
                // Calcule la teinte, la saturation et la valeur grâce à la nouvelle fonction new_RGBToHSV
                new_RGBToHSV(r, g, b, hsv);

                float hue = hsv[0]; // teinte
                float sat = hsv[1]; // saturation
                float val = hsv[2];

                if (hue <= 10 || hue >= 355) {
                    // Calcule r,g,b en fonction de la teinte, la saturation et de la valeur grâce à la nouvelle fonction new_HSVToRGB
                    pixel[i] = new_HSVToRGB(r, g, b, new float[]{hue, sat, val});
                } else {
                    int moy = (int) (0.3 * r + 0.59 * g + 0.11 * b);

                    pixel[i] = Color.rgb(moy, moy, moy);
                }
                i++;
            }
        }
        bmp.setPixels(pixel, 0, w, 0, 0, w, h);
    }

    // colorise : colore une image avec une couleur aléatoire
    static void colorize(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        int[] nb_pix = new int[w * h];
        int i = 0;

        // Choix du random
        Random c = new Random();
        float rc = c.nextFloat() * 360; // trouve un nombre random

        bmp.getPixels(nb_pix, 0, w, 0, 0, w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                int r = Color.red(nb_pix[i]);
                int g = Color.green(nb_pix[i]);
                int b = Color.blue(nb_pix[i]);


                float[] hsv = new float[3];
                new_RGBToHSV(r, g, b, hsv);

                // Applique la valeur random à la teinte
                hsv[0] = rc;
                float hue = hsv[0]; // teinte
                float sat = hsv[1]; // saturation
                float val = hsv[2];

                nb_pix[i] = new_HSVToRGB(r, g, b, new float[]{hue, sat, val});
                i++;
            }
        }
        bmp.setPixels(nb_pix, 0, w, 0, 0, w, h);
    }

    // new_RGBToHSV : calcule la teinte, la saturation et la valeur (permet un algorithme plus performant et plus rapide)
    static void new_RGBToHSV(int r, int g, int b, float[] hsv) {
        float r2 = r / 255.f;
        float g2 = g / 255.f;
        float b2 = b / 255.f;

        float rgbmax = Math.max(r2, g2);
        float cmax = Math.max(rgbmax, b2);

        float rgbmin = Math.min(r2, g2);
        float cmin = Math.min(rgbmin, b2);

        float d = cmax - cmin;

        float h = 0;

        if (d == 0) {
            h = 0;
        } else if (cmax == r2) {
            h = 60 * (((g2 - b2) % 6) / d);
        } else if (cmax == g2) {
            h = 60 * (((b2 - r2) / d) + 2);
        } else if (cmax == b2) {
            h = 60 * (((r2 - g2) / d) + 4);
        }

        float s = 0;

        if (cmax == 0) {
            s = 0;
        } else {
            s = d / cmax;
        }

        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = cmax;
    }

    // new_HSVToRGB : calcule à partie de la teinte, la saturation et la valeur les valeurs rouge, vert, bleu de l'image (permet un algorithme plus performant et plus rapide)
    static int new_HSVToRGB(int r, int g, int b, float[] hsv) {
        float c = hsv[2] * hsv[1];

        float x = c * (1 - Math.abs((hsv[0] / 60) % 2 - 1));

        float m = hsv[2] - c;

        float r2 = 0;
        float g2 = 0;
        float b2 = 0;

        if (hsv[0] >= 0f && hsv[0] < 60f) {
            r2 = c;
            g2 = x;
            b2 = 0;
        } else if (hsv[0] >= 60f && hsv[0] < 120f) {
            r2 = x;
            g2 = c;
            b2 = 0;
        } else if (hsv[0] >= 120f && hsv[0] < 180f) {
            r2 = 0;
            g2 = c;
            b2 = x;
        } else if (hsv[0] >= 180f && hsv[0] < 240f) {
            r2 = 0;
            g2 = x;
            b2 = c;
        } else if (hsv[0] >= 240f && hsv[0] < 300f) {
            r2 = x;
            g2 = 0;
            b2 = c;
        } else if (hsv[0] >= 300f && hsv[0] < 360f) {
            r2 = c;
            g2 = 0;
            b2 = x;
        }
        return Color.rgb((int) ((r2 + m) * 255), (int) ((g2 + m) * 255), (int) ((b2 + m) * 255));
    }

}
