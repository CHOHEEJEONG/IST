package com.example.nav.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nav.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import android.widget.Toast
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.radiobutton.MaterialRadioButton
import java.io.*
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import java.lang.StringBuilder
import android.graphics.drawable.BitmapDrawable
import android.R.attr.bitmap
import android.content.res.Resources
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Base64.NO_WRAP
import android.util.Base64.encodeToString
import android.util.Log
import com.example.nav.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.URI
import java.net.URL


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    lateinit var mainActivity: MainActivity2

    val BASE_URL = BuildConfig.BASE_URL

    private var croppedUri: Uri? = null

    var originalGroup: RadioGroup? = null
    var originalButton: RadioButton? = null
    var desiredGroup: RadioGroup? = null
    var desiredButton: RadioButton? = null
    var selectedOSeason: TextView? = null
    var selectedDSeason: TextView? = null

    // retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var server = retrofit.create(APIInterface::class.java)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity2

    }

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(1, 1)
                .getIntent(mainActivity)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private lateinit var  cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root : View = binding.root

        originalGroup = binding.originalSeason
        desiredGroup = binding.desiredSeason
        selectedOSeason = binding.selectedOSeason
        selectedDSeason = binding.selectedDSeason

        // 선택된 계절 표시된 textView 숨기기
        selectedOSeason!!.setVisibility(View.INVISIBLE)
        selectedDSeason!!.setVisibility(View.INVISIBLE)


        val btn_capture: Button = binding.btnCapture


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



        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                img_photo.setImageURI(uri)
                savePhoto(uri)
                croppedUri = uri
            }
        }

        btn_capture.setOnClickListener {
            cropActivityResultLauncher.launch(null)
        }

        return root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController: NavController = Navigation.findNavController(view)

        // 변환 버튼 클릭 시 웹으로 데이터 전송 및 변환 결과 보여주는 웹으로 이동
        btn_transfer.setOnClickListener(View.OnClickListener {

            if((selectedOSeason!!.length() == 0) ||(selectedDSeason!!.length() == 0)){
                Toast.makeText(mainActivity, "계절을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            } else if(img_photo.drawable == null) {
                Toast.makeText(mainActivity, "사진을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                //Toast.makeText(mainActivity, "Selected Season : " + selectedOSeason!!.text + "2" + selectedDSeason!!.text, Toast.LENGTH_SHORT).show()

                // retrofit
                // Image의 절대경로를 가져온다 : Uri to URL
                Log.e("croppedUri : ", croppedUri.toString())
                val imagePath: String = croppedUri.toString() // URL(절대경로)로 만들어줘야됨
                Log.e("imagePath : ", imagePath)


                // File변수에 File을 집어넣는다
                var file = File(URI(imagePath))
                Log.e("절대경로 : ", File(URI(imagePath)).toString())
                Log.e("절대경로 : ", File(imagePath).toString())

                var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                var body = MultipartBody.Part.createFormData("image", file.name, requestFile)


                val map : HashMap<String?, RequestBody?> = HashMap()
                val userInfo: RequestBody = RequestBody.create(MediaType.parse("text/plain"), user_name!!.text.toString())
                val origin: RequestBody = RequestBody.create(MediaType.parse("text/plain"), selectedOSeason!!.text.toString())
                val convert: RequestBody = RequestBody.create(MediaType.parse("text/plain"), selectedDSeason!!.text.toString())

                map.put("userInfo", userInfo)
                map.put("origin", origin)
                map.put("convert", convert)

                server.userEdit(body, map)?.enqueue(object: Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        Log.e("response : ", response?.body().toString())
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Log.e("response : ", "서버로 데이터 전송 실패했습니다.")
                    }
                })

                // 변환 결과 web 띄우기
                Toast.makeText(mainActivity, "변환 결과 페이지로 이동합니다.", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_navigation_home_to_transferFragment)
            }



            /*
            val selectedImage = bitmapToByteArray3()

            val webview = WebView(mainActivity)
            val dataUrl = "http://34.64.175.105:8080/community/create"

            //content에 Array 한번에 받아서 보내기, postData는 지울 예정
            val postData = "origin=${URLEncoder.encode(selectedOSeason!!.text  as String?, "UTF-8")}" +
                    "&convert=${URLEncoder.encode(selectedDSeason!!.text  as String?, "UTF-8")}" +
                    "&imgArray=${URLEncoder.encode(selectedImage.toString(), "UTF-8")}"

            val postContent = "content=${URLEncoder.encode(selectedImage.toString())}"

            webview.postUrl(dataUrl, postContent.toByteArray())
            Toast.makeText(mainActivity, "전송 완료", Toast.LENGTH_SHORT).show()


            //파일로 test 확인 : 지울 것
            val directory_name = "memo"
            val filename = "memo01.txt"
            (activity as MainActivity2).writeTextField(directory_name, filename, postData)*/



        })

    }

    // userID text 받아오기 위해 Activity에서 실행할 메서드
    fun changeTextView(string: String?){
        user_name.text = string
    }



    // 갤러리에 저장
    fun savePhoto(uri: Uri) {
        // 사진 폴더에 저장하기 위한 경로 선언
        val absolutePath = "/storage/emulated/0/"
        val folderPath = "$absolutePath/pictures/picSeason/"
        val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.jpeg"
        val folder = File(folderPath)
        if(!folder.isDirectory){ // 해당 경로에 폴더가 존재하지 않는다면
            folder.mkdir()
        }
        // 실제적인 저장 처리
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        Toast.makeText(mainActivity,"사진이 앨범에 저장되었습니다.",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}
