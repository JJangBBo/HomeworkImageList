# ImageList

<hr>

1. 이름 : 장보훈 <br>
2. 개발 환경: <br>
   OS -> window 7.
   <br>   
   IDE -> Eclipse.
   <br>
   Android Build Version -> 2.3.3
   <br>
   Open source -> LazyList.
   https://github.com/thest1/LazyList
   <br>
   
   기존내용을 수정 하여 사용하였으며 MIT License 라 해당 내용 고지 <br>
   수정내역<br>
   -> MemoryCache singleinstance class 로 수정하여 다른곳에서도 사용할 수 있도록 변경<br>
   -> File cache 제거하였으며, 제거 이유는 SD Card 사용에 따른 uses-permission 추가로 사용자에게 해당 내용을 고지 하는 화면이 필요하나 
   부재로 기능 제거 <br>
   -> bitmap 처리 부분중 resize 부분을 제거 하여 원본 비율대로 bitmap 을 구성하도록 수정 <br>
   <br>
    
   
   <br>
   
3. Retrospective review<br>
   revision -> 1.1.1 : Create Project<br><br>
   revision -> 1.2.1 : 1차 버전 업로드<br>
            -> API 를 통해 List 를 구성하여 화면 표시 구현<br>
	    -> LazyList 사용하여 URL 을 이미지 다운로드 후 화면 표시 구현<br>
	    -> 버그 사항 수정<br><br>
   revision -> 1.2.2 : 2차 버전 업로드<br>
            -> 상세 페이지 추가<br>
	    -> 이미지 비율구성을 위한 imageview 를 custom 으로 적용<br>
	    -> 버그 사항 수정<br><br>

<hr>
#Android Build Version<br>
2.3.3 으로 결정하는 이유<br>
통상의 최소 버전으로 제작 하여 해당 기능을 탑제 추가 하게 될 APP 에서 자유롭게 사용 할수 있도록 책정<br>

<hr>
#Android의 이미지로딩 라이브러리에 대한 고찰<br>

http://d2.naver.com/helloworld/429368

