package de.grau.organizer.classes;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Helper class for extracting needed information for an action
 * Extracts the Name, first phone number and email adress  from given intent if available
 *
 */

public class ContactHelper {
    private String id;
    private String name;
    private String number;
    private String email;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Extracts data from given intent in given context
     * data retrieved is stored in this class
     * @param context
     * @param data
     */
    public void contactPicked(Context context, Intent data) {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        cur.moveToFirst();
        try {
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cur = context.getContentResolver().query(uri, null, null, null, null);
            cur.moveToFirst();
            // column index of the contact ID
            this.id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
            // column index of the contact name
            this.name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            // column index of the phone number
            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                    new String[]{id}, null);
            while (pCur.moveToNext()) {
                this.number = pCur.getString(
                        pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            pCur.close();
            // column index of the email
            Cursor emailCur = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id}, null);
            while (emailCur.moveToNext()) {
                // This would allow you get several email addresses
                // if the email addresses were stored in an array
                this.email = emailCur.getString(
                        emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            }
            emailCur.close();
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
