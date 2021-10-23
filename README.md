# ImageStyleTransfer : PicSeason

### CycleGAN 알고리즘을 이용한 사진 계절 변환 하이브리드 앱 *(2021.07.10 - 2021.10.11)*

### 사용자의 요구에 따라 사용자의 사진 계절 변화 서비스 제공

+ 영상
<p align="center"><img width="50%" src="https://user-images.githubusercontent.com/83495586/138553339-d78ed77e-f3c3-4e7a-820e-e1c809fc2c8d.gif"/></p>


서버 : https://github.com/LambFerret/nodejsAndroid

CycleGAN : ImageCrawling, ML https://github.com/LambFerret/ProjectFinal

+ 기능

        회원가입 및 로그인
        
        계절 선택 및 사진 업로드
        
        사진 계절 변환
        
        변환된 사진 업로드 게시판 생성
        
        댓글 및 좋아요 기능
        
        게시글 수정 및 삭제


+ Activity, Fragment 구성

        MainActivity : 저장공간 및 카메라 권한 설정, 로그인/회원가입에 대한 WebView 형성, UserID를 받아오기 위한 JavascriptInterface 사용
        
        MainActivity2 : Fragment와 Navigation Bar를 담고 있는 Activity
        
        HomeFragment : UserID 표시, 사용자가 직접 변환하고자 하는 계절 선택, 카메라 및 갤러리로부터 사진 업로드, 변환버튼 클릭 시 서버로 데이터 전송(Retrofit) 및 변환 결과 WebView로 이동 
        
        TransferFragment : 머신닝 학습 모델을 사용하여 변환된 사진을 보여주는 WebView 형성
        
        CommunityFragment : 사용자들이 변환된 사진을 업로드하고 공유하며 소통할 수 있는 WebView 형성
        
        MypageFragment : Community에서 자신의 게시글만 볼 수 있는 WebView 형성
        
        SettingsFragment : 로그아웃, 사용 가이드, 회원탈퇴에 대한 WebView 형성
        
        APIInterface : 사용자가 선택한 계절과 Crop한 이미지를 서버로 전송하기 위해 RestAPI의 Retrofit 라이브러리 사용 (Multipart)
        
+ UI

        Android Jetpack Navigation 사용
        
        Home, Community, Mypage, Settings 페이지에 대한 Navigation Bar 형성
        
        Action Bar 위에 Page 정보 표시
        



