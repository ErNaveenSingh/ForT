private void getImDetailsFor(String phoneNumber)
    {
        //First step is to get the contact Id of the phone number
        //Create URI for it
    	Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
    	ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(
        		uri,
                new String[]{PhoneLookup._ID}, 
                Data.DATA1+"= ?", 
                new String[]{phoneNumber},
                null);
        String id="";
        while (cur.moveToNext	()) {
       	 	id = cur.getString(cur.getColumnIndex(PhoneLookup._ID));
       	 	Log.d("MYTAG", phoneNumber + ", id: " + id );
        }
        
        //Data.CONTENT_URI is the master URI to get all contact data
        //Send the Im.CONTACT_ID and Data.MIMETYPE
        Cursor pCur = cr.query(
       		 Data.CONTENT_URI, 
       		 null, 
       		 ContactsContract.CommonDataKinds.Im.CONTACT_ID +" = ? and "+ContactsContract.Data.MIMETYPE+" = ?",
       		 new String[]{id, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE}, 
       		 null);
       		 
        //Check this page :http://developer.android.com/reference/android/provider/ContactsContract.CommonDataKinds.Im.html#PROTOCOL
        //Here you will find Contant Values for dataType below
        // Eg. PROTOCOL_GOOGLE_TALK = 5
        //PROTOCOL_SKYPE = 3
        while (pCur.moveToNext	()) {
        	String dataType = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
        	String data = pCur.getString(pCur.getColumnIndex(ContactsContract.Data.DATA1));
       	 	Log.d("MYTAG", dataType + ", PN: " + data );
       	 }
    }
