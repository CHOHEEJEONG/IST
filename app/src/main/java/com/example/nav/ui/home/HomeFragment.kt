package com.example.nav.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nav.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import android.widget.Toast
import android.widget.RadioButton
import com.example.nav.MainActivity2
import android.widget.RadioGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.android.material.radiobutton.MaterialRadioButton
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    lateinit var mainActivity: MainActivity2

    var originalGroup: RadioGroup? = null
    var originalButton: RadioButton? = null
    var desiredGroup: RadioGroup? = null
    var desiredButton: RadioButton? = null
    var selectedOSeason: TextView? = null
    var selectedDSeason: TextView? = null


    val REQUEST_IMAGE_CAPTURE = 1  // 카메라 사진 촬영 요청 코드 *임의로 값 입력
    lateinit var currentPhotoPath : String // 문자열 형태의 사진 경로값 (초기값을 null로 시작하고 싶을 때 - lateinit var)
    val REQUEST_IMAGE_PICK = 10


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity2

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root : View = binding.root


        originalGroup = binding.originalSeason
        desiredGroup = binding.desiredSeason
        selectedOSeason = binding.selectedOSeason
        selectedDSeason = binding.selectedDSeason
        val btn_transfer: Button = binding.btnTransfer

        val btn_capture: Button = binding.btnCapture
        val btn_album: Button = binding.btnAlbum


        originalGroup?.setOnCheckedChangeListener { group, i ->
            val idx = group.indexOfChild(root.findViewById(group.checkedRadioButtonId))
            originalButton = group.getChildAt(idx) as MaterialRadioButton
            if(originalButton != null)
                selectedOSeason!!.text = originalButton!!.text.toString()
        }

        desiredGroup?.setOnCheckedChangeListener { group, i ->
            val idx = group.indexOfChild(root.findViewById(group.checkedRadioButtonId))
            desiredButton = group.getChildAt(idx) as MaterialRadioButton
            if(desiredButton != null)
                selectedDSeason!!.text = desiredButton!!.text.toString()
        }


        btn_transfer.setOnClickListener(View.OnClickListener {
            Toast.makeText(mainActivity, "Selected Season : " + selectedOSeason!!.text + "2" + selectedDSeason!!.text, Toast.LENGTH_SHORT).show()
            bitmapToByteArray()


            /*val selectedImage = bitmapToByteArray2()
            //val selectedImage = bitmapToByteArray3()

            val webview = WebView(mainActivity)
            val url = "http://34.64.143.233:8080/profile"

            val postData = "origin=${URLEncoder.encode(selectedOSeason!!.text  as String?, "UTF-8")}" +
                    "&convert=${URLEncoder.encode(selectedDSeason!!.text  as String?, "UTF-8")}" +
                    "&imgArray=${URLEncoder.encode(selectedImage.toString(), "UTF-8")}"

            webview.postUrl(url, postData.toByteArray())
            Toast.makeText(mainActivity, "전송 완료", Toast.LENGTH_SHORT).show()*/

        })

        btn_capture.setOnClickListener {
            takeCapture()
        }

        btn_album.setOnClickListener {
            getPhotoFromMyGallary()
        }

        (activity as MainActivity2).setPermission()

        return root

    }

    fun bitmapToByteArray() {
        val ivSource: ImageView = img_photo
        val ivCompressed: ImageView = iv_compressed

        try {
            val d = ivSource.drawable as BitmapDrawable
            val bitmap = d.bitmap

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            ivCompressed.setImageBitmap(compressedBitmap)
            Toast.makeText(
                mainActivity.applicationContext,
                "ByteArray created..",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Bitmap을 Byte로 변환
    fun bitmapToByteArray3(): ByteArray {
        val ivSource: ImageView = img_photo

        val d = ivSource.drawable as BitmapDrawable
        val bitmap = d.bitmap

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()

    }

    // 2 아니면 3
    fun bitmapToByteArray2(): Bitmap? {
        val ivSource: ImageView = img_photo

        val d = ivSource.drawable as BitmapDrawable
        val bitmap = d.bitmap

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return compressedBitmap

    }



    // 이미지를 바이트 배열로 변환 : 나중에 지우기
    @Throws(IOException::class)
    fun getBytes(`is`: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024 // 버퍼 크기
        val buffer = ByteArray(bufferSize) // 버퍼 배열
        var len = 0

        // InputStream에서 읽어올 게 없을 때까지 바이트 배열에 쓴다.
        while (`is`.read(buffer).also { len = it } != -1) byteBuffer.write(buffer, 0, len)
        return byteBuffer.toByteArray()
    }

    // 사진첩에서 사진 불러오기
    private fun getPhotoFromMyGallary() {
        Intent(Intent.ACTION_PICK).apply{
            type = "image/*"
            startActivityForResult(this,REQUEST_IMAGE_PICK)
        }
    }

    // 기본 카메라 앱을 사용해서 사진 촬영
    private fun takeCapture() {
        //기본 카메라 앱 실행
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(mainActivity.packageManager)?.also {
                val photoFile : File? = try{
                    createImageFile()
                }catch (e:Exception){
                    null
                }
                photoFile?.also {
                    val photoURI : Uri = FileProvider.getUriForFile(
                        mainActivity,
                        "com.example.nav.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    // 이미지 파일 생성
    private fun createImageFile(): File {
        val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_",".jpeg",storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    // startActivityForResult를 통해서 기본 카메라 앱으로 부터 받아온 결과값
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val bitmap : Bitmap
            val file = File(currentPhotoPath)
            if(Build.VERSION.SDK_INT < 28){// 안드로이드 9.0 보다 버전이 낮을 경우
                bitmap = MediaStore.Images.Media.getBitmap(mainActivity.contentResolver,Uri.fromFile(file))
                img_photo.setImageBitmap(bitmap)
            } else { // 안드로이드 9.0 보다 버전이 높을 경우
                val decode = ImageDecoder.createSource(
                    mainActivity.contentResolver,
                    Uri.fromFile(file)
                )
                bitmap = ImageDecoder.decodeBitmap(decode)
                img_photo.setImageBitmap(bitmap)
            }
            savePhoto(bitmap)
        }

        if(requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK){
            img_photo.setImageURI(data?.data)
        }
    }

    // 갤러리에 저장
    private fun savePhoto(bitmap: Bitmap) {
        // 사진 폴더에 저장하기 위한 경로 선언
        val absolutePath = "/storage/emulated/0/"
        val folderPath = "$absolutePath/pictures/"
        val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.jpeg"
        val folder = File(folderPath)
        if(!folder.isDirectory){ // 해당 경로에 폴더가 존재하지 않는다면
            folder.mkdir()
        }
        // 실제적인 저장 처리
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        Toast.makeText(mainActivity,"사진이 앨범에 저장되었습니다.",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}
