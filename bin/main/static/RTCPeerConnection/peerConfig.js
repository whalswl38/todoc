let localStreamElement = document.querySelector('#localStream');
const myKey = Math.random().toString(36).substring(2, 11);

let pcListMap = new Map();
let roomId = document.querySelector('#roomNumber').value;
console.log("roomId : " + roomId);

let otherKeyList = [];
let localStream = undefined;

let video2 = undefined;

const peerConnection = null;

//웹소켓 설정
const socket = new SockJS('/signaling');	//endpoint
stompClient = Stomp.over(socket);
stompClient.debug = null;

let pc = new RTCPeerConnection();


//음소거, 화면끄기
const muteBtn = document.getElementById("mute");
const cameraBtn = document.getElementById("camera");
const videoCam = document.getElementById("videoCam");
const mic = document.getElementById("mic");

let muted = false;
let cameraOff = false;

//캠 화면켜기
const startCam = async () =>{

	console.log("start Cam");
	
    if(navigator.mediaDevices !== undefined)
    {
    	// .then : (fucntion(mediaStream){ }) 처럼 받은 스트림 객체를 함수에 전달하고 그 함수 안에서 스트림을 통해서 필요한 기능을 구현합니다.
    	
        await navigator.mediaDevices.getUserMedia({ audio: true, video : true })
            .then(async (stream) => {	
            
                console.log('Stream found');
                localStream = stream;
                // Disable the microphone by default
                stream.getAudioTracks()[0].enabled = true;
                localStreamElement.srcObject = localStream;
                // Connect after making sure that local stream is availble

            }).catch(error => {
                console.error("Error accessing media devices:", error);
            });
    }
    
}


const connectSocket = () =>{
    //원래 위치
    //const socket = new SockJS('/signaling');	//endpoint
    //stompClient = Stomp.over(socket);
    //stompClient.debug = null;
	
	
    stompClient.connect({}, function () {
        console.log('Connected to WebRTC server');
   		
   		startStream();
   		
   		//subscribe: 특정 stomp 브로커의 location을 구독한다.
        stompClient.subscribe(`/topic/peer/iceCandidate/${myKey}/${roomId}`, candidate => {
            const key = JSON.parse(candidate.body).key
            const message = JSON.parse(candidate.body).body;
	
            pcListMap.get(key).addIceCandidate(new RTCIceCandidate({candidate:message.candidate,sdpMLineIndex:message.sdpMLineIndex,sdpMid:message.sdpMid}));
			
			
        });
		
		
		
        stompClient.subscribe(`/topic/peer/offer/${myKey}/${roomId}`, offer => {
            const key = JSON.parse(offer.body).key;
            const message = JSON.parse(offer.body).body;

            pcListMap.set(key,createPeerConnection(key));
            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription({type:message.type,sdp:message.sdp}));
            sendAnswer(pcListMap.get(key), key);
        });

        stompClient.subscribe(`/topic/peer/answer/${myKey}/${roomId}`, answer =>{
            const key = JSON.parse(answer.body).key;
            const message = JSON.parse(answer.body).body;

            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(message));

        });

        stompClient.subscribe(`/topic/call/key`, message =>{
            stompClient.send(`/app/send/key`, {}, JSON.stringify(myKey));
	
        });

        stompClient.subscribe(`/topic/send/key`, message => {
            const key = JSON.parse(message.body);

            if(myKey !== key && otherKeyList.find((mapKey) => mapKey === myKey) === undefined){
                otherKeyList.push(key);
            }
            
        });


    });
    
}


let onTrack = (event, otherKey) => {
	
    if(document.getElementById(`${otherKey}`) === null){
    	const video = document.querySelector('#remoteStream');
    	
        video.autoplay = true;
        video.id = otherKey;
        video.srcObject = event.streams[0];
	
		video2 = video;
    }
	
    //remoteStreamElement.srcObject = event.streams[0];
    // remoteStreamElement.play();
};



const createPeerConnection = (otherKey) =>{
    //const pc = new RTCPeerConnection();
    
    console.log("pearConnection입니다");
    
    	try {
        	pc.addEventListener('icecandidate', (event) =>{
            	onIceCandidate(event, otherKey);
        	});
        	pc.addEventListener('track', (event) =>{
            	onTrack(event, otherKey);
        	});
        	if(localStream !== undefined){
            	localStream.getTracks().forEach(track => {
                	pc.addTrack(track, localStream);
            	});
        	}

        	console.log('PeerConnection created');
    	} catch (error) {
        	console.error('PeerConnection failed: ', error);
    	}
    	
     document.querySelector('#endSteamBtn').style.display = '';
    
    return pc;
}




//offer 넘겨주기
let sendOffer = (pc ,otherKey) => {
    pc.createOffer().then(offer =>{
        setLocalAndSendMessage(pc, offer);
        stompClient.send(`/app/peer/offer/${otherKey}/${roomId}`, {}, JSON.stringify({
            key : myKey,
            body : offer
        }));
        console.log('Send offer');
    });
};


//answer 받고 넘겨주기
let sendAnswer = (pc,otherKey) => {
    pc.createAnswer().then( answer => {
        setLocalAndSendMessage(pc ,answer);
        stompClient.send(`/app/peer/answer/${otherKey}/${roomId}`, {}, JSON.stringify({
            key : myKey,
            body : answer
        }));
        console.log('Send answer');
    });
};


let onIceCandidate = (event, otherKey) => {
    if (event.candidate) {
        console.log('ICE candidate');
        stompClient.send(`/app/peer/iceCandidate/${otherKey}/${roomId}`,{}, JSON.stringify({
            key : myKey,
            body : event.candidate
        }));
    }
};



const setLocalAndSendMessage = (pc ,sessionDescription) =>{
    pc.setLocalDescription(sessionDescription);
    
}



//룸 번호 입력 후 캠 + 웹소켓 실행
/*
document.querySelector('#enterRoomBtn').addEventListener('click', async () =>{
    await startCam();


    roomId = document.querySelector('#roomIdInput').value;
    document.querySelector('#roomIdInput').disabled = true;
    document.querySelector('#enterRoomBtn').disabled = true;
	
	

    await connectSocket();
});
*/

    startCam();

    if(localStream !== undefined){
        document.querySelector('#localStream').style.display = 'block';
        document.querySelector('#startSteamBtn').style.display = '';
        //document.querySelector('#mute').style.display = '';
		//document.querySelector('#camera').style.display = '';
    }
    
    roomId = document.querySelector('#roomIdInput').value;
    document.querySelector('#roomIdInput').disabled = true;
	
	

    connectSocket();


// 스트림 버튼 클릭시 , 다른 웹 key들 웹소켓을 가져 온뒤에 offer -> answer -> iceCandidate 통신
// peer 커넥션은 pcListMap 으로 저장
/* 원래코드
document.querySelector('#startSteamBtn').addEventListener('click', async () =>{
	console.log("startsteamBtn click 실행");
    await stompClient.send(`/app/call/key`, {}, {});
    setTimeout(() =>{
*/


async function startStream(){
	console.log("startsteamBtn click 실행");
    await stompClient.send(`/app/call/key`, {}, {});
    setTimeout(() =>{
   	 otherKeyList.map((key) =>{
            if(!pcListMap.has(key)){
                pcListMap.set(key, createPeerConnection(key));
                sendOffer(pcListMap.get(key),key);
            }

        });

    },1000);
}

/*
//map.has 메서드 안에 들어있는 인수가 map 안에 들어있으면 true 반환 없으면 false 반환
        otherKeyList.map((key) =>{
            if(!pcListMap.has(key)){
                pcListMap.set(key, createPeerConnection(key));
                sendOffer(pcListMap.get(key),key);
            }

        });

    },1000);
});
*/


//강제 연결 끊기
document.querySelector('#endSteamBtn').addEventListener('click', () =>{
	pc.close();
	
	pc.addEventListener('iceconnectionstatechange', function(e) { 
   		console.log('ice 상태 변경', pc.iceConnectionState); 
	});
	
	alert("연결이 종료되었습니다.");
	location.href = "/mypage-page";
	
});




//음소거
function handleMuteClick(){
    localStream.getAudioTracks().forEach((track) => (track.enabled = !track.enabled));
    //위와같은 화살표함수
    //track.enabled라는 속성은 화면을 끄는 속성을 클릭할 때마다 서로 반대로 입력해주는 과정
    
    //localStream.getAudioTracks().forEach(function(track){
    //    track.enabled = !track.enabled;
    //})
    
    
    if(!muted){
        mic.innerText = "mic_off";
        muted = true;
    } else{
        mic.innerText = "mic";
        muted = false;
    }
    
}

//화면끄기
function handleCameraClick(){
    localStream.getVideoTracks().forEach((track) => (track.enabled = !track.enabled));
    if(cameraOff){
        videoCam.innerText = "videocam";
        cameraOff = false;
    } else {
        videoCam.innerText = "videocam_off";
        cameraOff = true;
    }
   
}


muteBtn.addEventListener("click", handleMuteClick);
cameraBtn.addEventListener("click", handleCameraClick);


//연결상태 변경을 알리는 이벤트
pc.addEventListener('iceconnectionstatechange', function(e) { 
   console.log('ice 상태 변경', pc.iceConnectionState);
   
   handleChangeIcecandidate(pc.iceConnectionState)
});


function handleChangeIcecandidate(change){
	if(change == "disconnected")	
	{
		alert("연결이 종료되었습니다.");
		location.href = "/mypage-page";
	}
};