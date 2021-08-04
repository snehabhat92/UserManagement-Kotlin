package com.example.myapplication.View

import android.Manifest
import android.app.AlertDialog
import android.content.ContentProviderOperation
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.View.WebActivity
import com.example.myapplication.util.Util.Companion.hideKeyboard
import com.example.myapplication.Model.UserParcel
import com.example.myapplication.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class ProfileFragment : Fragment(),
    View.OnClickListener,
    OnMapReadyCallback {
    private var txtPhone: TextView? = null
    private var txtEmail: TextView? = null
    private var txtWebsite: TextView? = null
    private var txtFirstName: TextView? = null
    private var txtLastName: TextView? = null
    private var txtUserName: TextView? = null
    private var txtAddress: TextView? = null
    private var txtCompany: TextView? = null
    private lateinit var profileView: View
    private var lat = 0.0
    private var lng = 0.0
    private var mapView: MapView? = null
    private lateinit var btnSave: Button
    private var progressBar: ProgressBar? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileView = inflater.inflate(R.layout.fragment_profile, container, false)
        if (activity != null) {
            activity!!.title = resources.getString(R.string.profile)
        }
        initialiseUI()
        hideKeyboard(activity)
        return profileView
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initialiseUI() {
        mapView = profileView.findViewById(R.id.map)
        if (mapView != null) {
            mapView!!.onCreate(null)
            mapView!!.onResume()
            mapView!!.getMapAsync(this)
        }
        txtFirstName = profileView.findViewById(R.id.txt_first_name)
        txtLastName = profileView.findViewById(R.id.txt_last_name)
        txtUserName = profileView.findViewById(R.id.txt_user_name)
        txtPhone = profileView.findViewById(R.id.txt_phone)
        val imgPhone =
            profileView.findViewById<ImageView>(R.id.img_phone)
        txtEmail = profileView.findViewById(R.id.txt_email)
        val imgEmail =
            profileView.findViewById<ImageView>(R.id.img_email)
        txtAddress = profileView.findViewById(R.id.txt_address)
        txtWebsite = profileView.findViewById(R.id.txt_website)
        val imgWebsite =
            profileView.findViewById<ImageView>(R.id.img_website)
        txtCompany = profileView.findViewById(R.id.txt_company)
        btnSave = profileView.findViewById(R.id.btn_save)
        progressBar = profileView.findViewById(R.id.progress_bar)
        setUpData()
        imgPhone.setOnClickListener(this)
        imgEmail.setOnClickListener(this)
        imgWebsite.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpData() {
        val bundle = arguments
        if (bundle != null && bundle.containsKey("User")) {
            val userParcel: UserParcel? = bundle.getParcelable("User")
            if (userParcel != null) {
                val firstName: String? = userParcel.firstName
                if (!TextUtils.isEmpty(firstName) && txtFirstName != null) {
                    txtFirstName!!.text = firstName
                }
                val lastName: String? = userParcel.lastName
                if (!TextUtils.isEmpty(lastName) && txtLastName != null) {
                    txtLastName!!.text = lastName
                }
                val userName: String? = userParcel.userName
                if (!TextUtils.isEmpty(userName) && txtUserName != null) {
                    txtUserName!!.text = userName
                }
                val phone: String? = userParcel.phone
                if (!TextUtils.isEmpty(phone) && txtPhone != null) {
                    txtPhone!!.text = PhoneNumberUtils.formatNumber(phone, "US")
                }
                val email: String? = userParcel.email
                if (!TextUtils.isEmpty(email) && txtEmail != null) {
                    txtEmail!!.text = email
                }
                val website: String? = userParcel.website
                if (!TextUtils.isEmpty(website) && txtWebsite != null) {
                    txtWebsite!!.text = website
                }
                val company: String? = userParcel.company
                if (!TextUtils.isEmpty(company) && txtCompany != null) {
                    txtCompany!!.text = company
                }
                val address: String? = userParcel.address
                if (!TextUtils.isEmpty(address) && txtAddress != null) {
                    txtAddress!!.text = address
                }
                lat = userParcel.lat
                lng = userParcel.lng
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
        handleSaveButton()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun handleSaveButton() {
        if (activity == null || TextUtils.isEmpty(txtPhone!!.text.toString())) {
            btnSave.visibility = View.GONE
            return
        }
        if (!isContactAccessible) {
            btnSave.visibility = View.GONE
            return
        }
        val phone = PhoneNumberUtils.normalizeNumber(txtPhone!!.text.toString())
        val lookupUri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phone)
        )
        val mPhoneNumberProjection = arrayOf(
            ContactsContract.PhoneLookup._ID,
            ContactsContract.PhoneLookup.NUMBER,
            ContactsContract.PhoneLookup.DISPLAY_NAME
        )
        val cur = activity!!.contentResolver
            .query(lookupUri, mPhoneNumberProjection, null, null, null)
        cur.use { cur ->
            if (cur!!.moveToFirst()) {
                cur.close()
                btnSave.visibility = View.GONE
            }
        }
    }

    private val isContactAccessible: Boolean
        get() {
            if (activity == null) return false
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.WRITE_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS
                    ), REQUEST_CODE
                )
                return false
            }
            btnSave.visibility = View.VISIBLE
            return true
        }

    private fun showPermissionDialog(message: String) {
        val builder =
            AlertDialog.Builder(activity)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok
        ) { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            val permission = permissions[0]
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                val showRationale = shouldShowRequestPermissionRationale(permission)
                if (!showRationale) {
                    showPermissionDialog(resources.getString(R.string.permission_to_save_contact))
                } else if (Manifest.permission.WRITE_CONTACTS == permission || Manifest.permission.READ_CONTACTS == permission) {
                    showPermissionDialog(resources.getString(R.string.provide_permission))
                }
            } else {
                btnSave.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_phone -> launchActivity(v.id)
            R.id.img_email -> launchActivity(v.id)
            R.id.img_website -> launchActivity(v.id)
            R.id.btn_save -> saveContact()
        }
    }

    private fun launchActivity(id: Int) {
        var intent: Intent? = null
        if (id == R.id.img_phone) {
            intent = Intent(Intent.ACTION_DIAL)
            if (!TextUtils.isEmpty(txtPhone!!.text.toString())) {
                intent.data = Uri.parse("tel:" + txtPhone!!.text.toString())
            }
        } else if (id == R.id.img_email) {
            var email = ""
            if (!TextUtils.isEmpty(txtEmail!!.text.toString())) {
                email = txtEmail!!.text.toString()
            }
            intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
        } else {
            var website = ""
            if (!TextUtils.isEmpty(txtWebsite!!.text.toString())) {
                website = txtWebsite!!.text.toString()
            }
            intent = Intent(activity, WebActivity::class.java)
            intent.putExtra("url", website)
        }
        if (activity != null && activity!!.packageManager != null && intent?.resolveActivity(
                activity!!.packageManager
            ) != null
        ) {
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun saveContact() {
        progressBar!!.visibility = View.VISIBLE
        btnSave.visibility = View.GONE
        insertContact(
            txtUserName!!.text.toString(),
            txtPhone!!.text.toString(),
            txtEmail!!.text.toString(),
            txtWebsite!!.text.toString()
        )
        progressBar!!.visibility = View.GONE
        btnSave.visibility = View.GONE
        Toast.makeText(
            activity,
            resources.getString(R.string.contact_saved_successfully),
            Toast.LENGTH_SHORT
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun insertContact(
        displayName: String,
        phoneNumber: String,
        email: String,
        website: String
    ) {
        val ops =
            ArrayList<ContentProviderOperation>()
        ops.add(
            ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI
            )
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )
        if (!TextUtils.isEmpty(displayName)) {
            ops.add(
                ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI
                )
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        displayName
                    ).build()
            )
        }
        if (!TextUtils.isEmpty(phoneNumber)) {
            val mobileNumber = PhoneNumberUtils.normalizeNumber(phoneNumber)
            if (!TextUtils.isEmpty(mobileNumber)) {
                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        )
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                        .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                        )
                        .build()
                )
            }
        }
        if (!TextUtils.isEmpty(email)) {
            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(
                        ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK
                    )
                    .build()
            )
        }
        if (!TextUtils.isEmpty(website)) {
            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Website.URL, website)
                    .withValue(
                        ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Website.TYPE_WORK
                    )
                    .build()
            )
        }
        try {
            activity!!.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(lat, lng)
        googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Marker")
        )
        try {
            MapsInitializer.initialize(activity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 0f))
    }

    override fun onDestroy() {
        if (mapView != null) {
            mapView!!.onDestroy()
        }
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        if (mapView != null) mapView!!.onResume()
    }

    override fun onPause() {
        if (mapView != null) mapView!!.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (mapView != null) mapView!!.onLowMemory()
    }

    companion object {
        private const val REQUEST_CODE = 101
    }
}