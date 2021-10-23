# ImageStyleTransfer : PicSeason

CycleGAN 알고리즘을 이용한 사진 계절 변환 하이브리드 앱

서버 : https://github.com/LambFerret/nodejsAndroid

CycleGAN : ImageCrawling, ML https://github.com/LambFerret/ProjectFinal

Activity, Fragment 구성

MainActivity : 저장공간 및 카메라 권한 설정, 로그인/회원가입에 대한 WebView 형성, UserID를 받아오기 위한 JavascriptInterface 사용

MainActivity2 : Fragment와 Navigation Bar를 담고 있는 Activity

HomeFragment : UserID 표시, 사용자가 직접 변환하고자 하는 계절 선택, 카메라 및 갤러리로부터 사진 업로드, 변환버튼 클릭 시 서버로 데이터 전송(Retrofit) 및 변환 결과 WebView로 이동 

TransferFragment : 머신닝 학습 모델을 사용하여 변환된 사진을 보여주는 WebView 형성

CommunityFragment : 사용자들이 변환된 사진을 업로드하고 공유하며 소통할 수 있는 WebView 형성

MypageFragment : Community에서 자신의 게시글만 볼 수 있는 WebView 형성

SettingsFragment : 로그아웃, 사용 가이드, 회원탈퇴에 대한 WebView 형성

APIInterface : 사용자가 선택한 계절과 Crop한 이미지를 서버로 전송하기 위해 RestAPI의 Retrofit 라이브러리 사용 (Multipart)
