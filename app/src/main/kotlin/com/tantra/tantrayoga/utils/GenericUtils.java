package com.tantra.tantrayoga.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class GenericUtils {

    private GenericUtils() {
    }

    private static final String LOG_TAG = "GenericUtils";
    private static final int BUFFER_SIZE = 2048;

    private static final float MILES_CONVERSION_FACTOR = 0.000621371192f; // 1/1609.344
    private static final float KILOMETERS_CONVERSION_FACTOR = 0.001f;

    /**
     * Creates a new temp file in external memory
     *
     * @param context   context
     * @param fileName  the file name
     * @param extension the file extension
     * @return the new file or null if it fails to create a new file
     * @deprecated use {@link }
     */
    @Deprecated
    public static File getTemporaryNewPhotoFile(Context context, String fileName,
                                                String extension) {
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        final String imageFileName = fileName + "_" + timeStamp;
        final File storageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/"
                                                 + context.getApplicationContext()
                .getPackageName());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName,  /* prefix */
                    "." + extension,
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * Validates and instantiate the view by resource id
     *
     * @param activity current activity
     * @param viewId   view Id
     * @param <T>      view type.
     * @return view
     * @throws ClassCastException       is the view is not of class T
     * @throws IllegalArgumentException if the view with the given id is not present
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(Activity activity, @IdRes int viewId) {
        return (T) checkView(activity.findViewById(viewId));
    }

    /**
     * Validates and instantiate the view by resource id
     *
     * @param parent parent view
     * @param viewId view Id
     * @param <T>    view type
     * @return view
     * @throws ClassCastException       is the view is not of class T
     * @throws IllegalArgumentException
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(View parent, @IdRes int viewId) {
        return (T) checkView(parent.findViewById(viewId));
    }

    /**
     * @throws IllegalArgumentException is view is null
     */
    @NonNull
    private static View checkView(View v) {
        if (v == null) {
            throw new IllegalArgumentException("View doesn't exist");
        }
        return v;
    }

    /**
     * @deprecated use java.util.Collections#swap(java.util.List, int, int)
     */
    @Deprecated
    public static <E> void swapValuesInList(List<E> list, int firstIndex, int secondIndex) {
        E temp = list.get(firstIndex);
        list.set(firstIndex, list.get(secondIndex));
        list.set(secondIndex, temp);
    }

    // defaults with 0
    public static int stringToInt(String intString) {
        return stringToInt(intString, 0);
    }

    public static int stringToInt(String intString, int defaultValue) {
        int result = defaultValue;
        try {
            intString = intString.replaceAll(",", "");
            intString = intString.replaceAll("\\.", "");
            result = Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "'" + intString + "' is not a valid integer");
        }
        return result;
    }

    // defaults with 0
    public static double stringToDouble(String doubleString) {
        return stringToDouble(doubleString, 0.0);
    }

    public static double stringToDouble(String doubleString, double defaultValue) {
        double result = defaultValue;
        try {
            result = Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "'" + doubleString + "' is not a valid double value");
        }
        return result;
    }

    /**
     * Deletes a given file.
     *
     * @param filePath the path of the file to delete
     * @return true if deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        boolean result = false;
        if (!TextUtils.isEmpty(filePath)) {
            try {
                result = (new File(filePath)).delete();
            } catch (SecurityException e) {
                Log.e(LOG_TAG,
                      "Fail to delete the '" + filePath + "' for security manager check fails");
            }
        }
        return result;
    }

    /**
     * Deletes a given files.
     *
     * @param filePaths a list of paths to delete
     * @return true if all files are deleted, false otherwise
     */
    public static boolean deleteFiles(Collection<String> filePaths) {
        boolean result = true;
        for (String filePath : filePaths) {
            result &= deleteFile(filePath);
        }
        return result;
    }

    public static void copyFile(InputStream input, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        OutputStream output = new FileOutputStream(destFile);

        byte[] buffer = new byte[2048];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        input.close();
        output.close();
    }

    public static boolean copyFile(File sourceFile, File destFile) {
        FileChannel source = null;
        FileChannel destination = null;
        try {
            destFile.getParentFile().mkdirs();
            source = new RandomAccessFile(sourceFile, "rw").getChannel();
            destination = new RandomAccessFile(destFile, "rw").getChannel();

            long position = 0;
            long count = source.size();

            source.transferTo(position, count, destination);
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "Fail to copy file", e);
            return false;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Fail to copy file", e);
            return false;
        } finally {
            closeSilently(source);
            closeSilently(destination);
        }
        return true;
    }

    public static void copyFile(Context context, Uri source, Uri destination) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(source);
        OutputStream outputStream = contentResolver.openOutputStream(destination);

        if (inputStream == null || outputStream == null) {
            throw new IOException("No valid streams");
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        int count;
        while ((count = inputStream.read(buffer, 0, BUFFER_SIZE)) > 0) {
            outputStream.write(buffer, 0, count);
        }

        closeSilently(outputStream);
        closeSilently(inputStream);
    }

    /**
     * Reads all the asset file contents into a string
     *
     * @param context from here the assets are located
     * @param name    of the asset file
     * @return string with the asset text content
     */
    public static String loadAssetTextAsString(Context context, String name) {
        BufferedReader reader = null;
        String result = null;
        AssetManager assetManager = context.getAssets();

        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream is = assetManager.open(name);
            reader = new BufferedReader(new InputStreamReader(is));
            char[] buffer = new char[1024];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, length);
            }
            result = stringBuilder.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error opening asset " + name);
        } finally {
            closeSilently(reader);
        }

        return result;
    }

    /**
     * Joins a string array into a single string using a delimiter. It will skip the string array element if it it null or empty.
     *
     * @param delimiter to be used to join the string
     * @param elements  to join into a single string
     * @return a string that result from the concatenation of all given valid string array elements
     */
    public static String join(String delimiter, CharSequence... elements) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (CharSequence field : elements) {
            if (!TextUtils.isEmpty(field)) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }
                sb.append(field);
            }
        }
        return sb.toString();
    }

    /**
     * @see {@link #join(String, CharSequence[])}
     **/
    public static String join(String delimiter, Iterable<? extends CharSequence> elements) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (CharSequence field : elements) {
            if (!TextUtils.isEmpty(field)) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }
                sb.append(field);
            }
        }
        return sb.toString();
    }

    /**
     * @see {@link #join(String, CharSequence[])}
     **/
    public static String joinToString(String delimiter, Iterable<?> elements) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object field : elements) {
            if (field != null && !TextUtils.isEmpty(field.toString())) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }
                sb.append(field);
            }
        }
        return sb.toString();
    }

    /**
     * Report if the device has a hardware menu key available
     *
     * @param context The application context used to to get the configuration
     * @return true if the device has a hardware menu key, false otherwise
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasHardMenuKey(Context context) {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                        ViewConfiguration.get(context).hasPermanentMenuKey());
    }

    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     *
     * @param context   Used to find the correct file
     * @param fieldName The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    public static <T> T getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            return getBuildConfigValue(clazz, fieldName);
        } catch (ClassNotFoundException e) {
            Log.w(LOG_TAG, "Error getting BuildConfig '" + fieldName + "' value");
        }
        return null;
    }

    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     *
     * @param buildConfigClazz BuildConfig class
     * @param fieldName        The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBuildConfigValue(Class<?> buildConfigClazz, String fieldName) {
        try {
            Field field = buildConfigClazz.getField(fieldName);
            return (T) field.get(null);
        } catch (NoSuchFieldException e) {
            Log.w(LOG_TAG, "Error getting BuildConfig '" + fieldName + "' value");
        } catch (IllegalAccessException e) {
            Log.w(LOG_TAG, "Error getting BuildConfig '" + fieldName + "'value");
        }
        return null;
    }

    /**
     * Opens the dial application with the given number pre-filled
     *
     * @param number is the phone number
     */
    public static void dialPhoneNumber(Context context, String number) {
        if (number != null) {
            try {
                // is not ACTION_DIAL because other phone apps (skype - old versions - only seem to only catch the ACTION_VIEW)
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tel:" + number));
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "R.string.no_phone_application", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(LOG_TAG, "Null number");
        }
    }

    /**
     * {@code force} defaults to {@code false}.
     *
     * @see #showSoftKeyboard(Context, View, boolean)
     */
    public static void showSoftKeyboard(Context context, View view) {
        showSoftKeyboard(context, view, false);
    }

    /**
     * This method takes a View in which the user should type something, calls requestFocus()
     * to give it focus, then showSoftInput() to open the input method.
     * <p>
     * <p>It should be called using the {@link View#post(Runnable)}, or any of its variants, to avoid focus problem and UI lag</p>
     *
     * @param view view to give focus
     */
    public static void showSoftKeyboard(Context context, View view, boolean force) {
        if (view.requestFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, force ? 0 : InputMethodManager.SHOW_IMPLICIT);
        }
    }


    /**
     * {@code force} defaults to {@code false}.
     *
     * @see #hideSoftKeyboard(Context, View, boolean)
     */
    public static void hideSoftKeyboard(Context context, View view) {
        hideSoftKeyboard(context, view, false);
    }

    /**
     * This method takes a (any) View from the current layout and closes the input method.
     * <p>
     * <p><strong>Note</strong> Once the input method is visible, you should not programmatically hide it.
     * The system hides the input method when the user finishes the task in the text field or
     * the user can hide it with a system control (such as with the Back button).
     * </p>
     * <p>
     * <p>It should be called using the {@link View#post(Runnable)}, or any of its variants, to avoid focus problem and UI lag</p>
     *
     * @see <a href="Handling Input Method Visibility">
     * http://developer.android.com/training/keyboard-input/visibility.html#ShowOnDemand
     * </a>
     */
    public static void hideSoftKeyboard(Context context, View view, boolean force) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                                                       force ? 0 : InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    /**
     * Open navigation application, to the give destination
     * <p>
     * <p>(fallback to open google maps on browser)</p>
     *
     * @param to geo location of the destination
     */
    public static void openNavigation(Context context, Location to) {
        if (to != null) {
            openNavigation(context, to.getLatitude() + "," + to.getLongitude());
        } else {
            Log.e(LOG_TAG, "Null location");
        }
    }

    /**
     * Open navigation (maps on navigation mode) application, to the give destination.
     * <p>
     * <p>(fallback to open google maps on browser)</p>
     *
     * @param address of the destination
     */
    public static void openNavigation(Context context, String address) {
        address = Uri.encode(address);
        try {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=" + address)
            );
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                // fallback to browser
                intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + address)
                );
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "R.string.no_maps_application", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Open the maps application to show the direction from the given origin to given destination
     *
     * @param origin      geo location of the origin
     * @param destination geo location of the destination
     */
    public static void openDirections(Context context, Location origin, Location destination) {
        String originAddress = origin.getLatitude() + "," + origin.getLongitude();
        String destinationAddress = destination.getLatitude() + "," + destination.getLongitude();
        openDirections(context, originAddress, destinationAddress);
    }

    /**
     * Open the maps application to show the direction from the given origin to given destination
     *
     * @param originAddress      address of the origin
     * @param destinationAddress address of the destination
     */
    public static void openDirections(Context context, String originAddress,
                                      String destinationAddress) {
        try {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                            "http://maps.google.com/maps?saddr=" + originAddress + "&daddr=" + destinationAddress)
            );
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "R.string.no_maps_application", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Open maps application with the given location marked on map.
     * <p>
     * <p>(fallback to open google maps on browser)</p>
     *
     * @param location to be marked and to be the center of the map
     */
    public static void openMap(Context context, Location location) {
        if (location != null) {
            String address = location.getLatitude() + "," + location.getLongitude();
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:" + address)
            );
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                // fallback to browser
                intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=" + address)
                );
                context.startActivity(intent);
            }
        } else {
            Log.e(LOG_TAG, "Null location");
        }
    }

    /**
     * Open maps application with the given address marked on map.
     * <p>
     * <p>(fallback to open google maps on browser)</p>
     *
     * @param address to be marked and to be the center of the map
     */
    public static void openMap(Context context, String address) {
        try {
            address = Uri.encode(address);
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=" + address)
            );
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                // fallback to browser
                intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=" + address)
                );
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "R.string.no_browser_application", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Opens the email application to compose an email to the given address
     *
     * @param emailAddress is the email address
     */
    public static void openEmail(Context context, String emailAddress) {
        if (emailAddress != null) {
            try {
                String[] emails = {emailAddress};
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, emails);
                intent.setType("message/rfc822");
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "R.string.no_email_application", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(LOG_TAG, "Null number");
        }
    }

    public static void closeSilently(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static File createNewUniqueFile(String prefix, String suffix, File directory)
            throws IOException {
        // Force a prefix null check first
        if (prefix.length() < 3) {
            throw new IllegalArgumentException("prefix must be at least 3 characters");
        }
        if (suffix == null) {
            suffix = ".tmp";
        }
        File tmpDirFile = directory;
        if (tmpDirFile == null) {
            String tmpDir = System.getProperty("java.io.tmpdir", ".");
            tmpDirFile = new File(tmpDir);
        }
        File result;
        do {
            result = new File(tmpDirFile, prefix + System.currentTimeMillis() + suffix);
        } while (!result.createNewFile());
        return result;

    }

    /**
     * Computes the MD5 hash of a given string
     *
     * @param s is the input
     * @return the MD5 hash
     */
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "No MD5 implementation");
        }
        return "";
    }

    /**
     * Will merge a set of arrays of the same type into a single array (it ignores any null element)
     *
     * @param arrays is the set of arrays
     * @param <T>    if the type of the arrays and result array
     * @return a new array with all merged elements
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> T[] merge(T[]... arrays) {
        if (arrays == null) {
            throw new NullPointerException();
        }

        if (arrays.length == 0) {
            return (T[]) Array
                    .newInstance(arrays.getClass().getComponentType().getComponentType(), 0);
        }

        long size = 0;
        int arraysNumber = arrays.length;
        for (int i = 0; i < arraysNumber; i++) {
            T[] entry = arrays[i];
            if (entry == null) {
                continue;
            }
            size += entry.length;
            if (size > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Resulting array is too big");
            }
        }
        int resultSize = (int) size;

        T[] result = Arrays.copyOf(arrays[0], resultSize);
        int nextPos = arrays[0].length;

        for (int i = 1; i < arraysNumber; i++) {
            T[] entry = arrays[i];
            if (entry == null) {
                continue;
            }
            System.arraycopy(entry, 0, result, nextPos, entry.length);
            nextPos += entry.length;
        }

        return result;
    }

    /**
     * Opens the file using the appropriated application according with its file extension.
     *
     * @param file is the local file (public access)
     */
    public static void openFile(Context context, File file) {
        openFile(context, Uri.fromFile(file).toString());
    }

    /**
     * Opens the file using the appropriated application according with its file extension.
     *
     * @param fileUri is the location of the file (public access)
     */
    public static void openFile(Context context, String fileUri) {
        Uri uri = Uri.parse(fileUri);
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());

        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            intent.setDataAndType(uri, mimeType);
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "R.string.no_application_for_file",
                           Toast.LENGTH_SHORT).show();

        }
    }


    public static boolean equals(Object obj1, Object obj2) {
        return obj1 == obj2 || obj1 != null && obj1.equals(obj2);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static float metersToMiles(float meters) {
        return meters * MILES_CONVERSION_FACTOR;
    }

    public static float metersToKilometers(float meters) {
        return meters * KILOMETERS_CONVERSION_FACTOR;
    }

    /**
     * Tints the drawable with the given colour
     *
     * @return the tinted drawable
     */
    public static Drawable tintWithColor(Drawable drawable, @ColorInt int color) {
        Drawable result = drawable.mutate();
        result = DrawableCompat.wrap(result);
        DrawableCompat.setTint(result, color);
        return result;
    }

    /**
     * Tints the drawable with the given colour
     *
     * @return the tinted drawable
     */
    public static Drawable tintWithColor(Context context, Drawable drawable,
                                         @ColorRes int colorRes) {
        return tintWithColor(drawable, ContextCompat.getColor(context, colorRes));
    }

    /**
     * Returns the int value of the long argument safely
     * @throws ArithmeticException if the value overflows an int
     */
    public static int longToIntExact(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new ArithmeticException("the value overflows an int");
        }
        return (int) l;
    }

    /**
     * Remove extra whitespace (normally added by {@link Html#fromHtml(String)} ()}
     */
    public static CharSequence trimCharSequence(CharSequence s) {
        int start = 0;
        int end = s.length();

        while (start < end && isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }

    private static boolean isWhitespace(char c) {
        return Character.isWhitespace(c) || c == 160;
    }

}
