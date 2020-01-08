#pragma  version (1)
#pragma  rs  java_package_name(com.example.sushi)

float hue = 0;
static float4 to_gray = {0.299f, 0.587f, 0.114f, 0.0f};

// HSV / RGV

static float3 RGBToHSV(float4 in) {
    float r, g, b;
    float3 p = {in.r, in.g, in.b};
    r = p.r / 255.0f;
    g = p.g / 255.0f;
    b = p.b / 255.0f;

    float rgbmax = fmax(r, g);
    float cmax = fmax(rgbmax, b);

    float rgbmin = fmin(r, g);
    float cmin = fmin(rgbmin, b);

    float d = cmax - cmin;  // faire gaffe à la div par 0

    float h = 0.0f;

    if (d == 0) {
        h = 0.0f;
    } else if (cmax == r) {
        h = 60 * fmod(((g - b) / d), 6);
    } else if (cmax == g) {
        h = 60 * (((g - b) / d) + 2);
    } else if (cmax == b) {
        h = 60 * (((r - g) / d) + 4);
    }

    float s;

    if (cmax == 0) {
        s = 0.0f;
    } else {
        s = d / cmax;
    }

    float3 hsv = {fabs(h),fabs(s),fabs(cmax)};

    return hsv;
}

static float3 HSVToRGB(float3 hsv) {
    float c = hsv.b * hsv.g;

    float x = c * (1 - fabs(fmod((hsv.r / 60), 2) - 1));

    float m = hsv.b - c;

    float r = 0.0f;
    float g = 0.0f;
    float b = 0.0f;

    if (hsv.r >= 0 && hsv.r < 60.0f) {
        r = c;
        g = x;
        b = 0.0f;
    } else if (hsv.r >= 60 && hsv.r < 120) {
        r = x;
        g = c;
        b = 0.0f;
    } else if (hsv.r >= 120 && hsv.r < 180) {
        r = 0.0f;
        g = c;
        b = x;
    } else if (hsv.r >= 180 && hsv.r < 240) {
        r = 0.0f;
        g = x;
        b = c;
    } else if (hsv.r >= 240 && hsv.r < 300) {
        r = x;
        g = 0.0f;
        b = c;
    } else if (hsv.r >= 300 && hsv.r < 360) {
        r = c;
        g = 0.0f;
        b = x;
    }

    float r2 = (r+m)*255.0f;
    float g2 = (g+m)*255.0f;
    float b2 = (b+m)*255.0f;

    float3 color = {r2, g2, b2};

    return color;
}

// colorize

uchar4 RS_KERNEL colorize(uchar4 in) {
    const float4 pix = rsUnpackColor8888(in);

    float3 hsv = RGBToHSV(pix);

    hsv.x = hue; //v_rand;

    const float3 out = HSVToRGB(hsv);
    return rsPackColorTo8888(out.r, out.g, out.b, 255.0f);
}