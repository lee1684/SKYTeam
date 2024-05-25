const accessToken = "Bearer " + getCookie('access'); // header or cookie 사용

// 소켓 연결을 위한 객체 생성
const stompClient = new StompJs.Client({
    brokerURL: 'wss://ssalon.co.kr/ws-stomp',
    connectHeaders: {
        Authorization: accessToken,
        moimId: 1,
    },
});

// 소켓 연결 시도
function connect() {
    stompClient.activate();
}

// 소켓 연결이 된 직후 채팅방 구독(subscribe)
stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/room/1', (greeting) => { // 특정 모임 아이디로 설정
        showMessage(JSON.parse(greeting.body));
    });
};

// 메시지 전송(publish)
function sendMessage() {
    stompClient.publish({
        destination: "/send/1", // 특정 모임 아이디로 설정
        headers: { "Authorization": accessToken },
        body: JSON.stringify({
            'message': $("#message").val(),
        })
    });
}

// 메시지를 html에 출력
function showMessage(messageObj) { // messageObj = nickname, message, profilePicture, date
    $("#greetings").append("<tr><td>" + messageObj.nickname + " : " + messageObj.message + "</td></tr>");
}

// 기존 채팅 불러오기
function loadChat(chatList) {
    // GET /api/chat/{moimId} API 호출
}

// 채팅방에 참여하고 있는 회원 조회
function loadConnectedMember() {
    // GET /api/chat/{moimId}/users API 호출
}

// 소켓 연결 여부에 따른 UI 변경
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

// 소켓 연결 해제
function disconnect() {
    stompClient.publish({
        destination: "/send/disconnect",
        headers: {
            "Authorization": accessToken,
        },
    })
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

// 에러 핸들링
stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

// 에러 핸들링
stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

// 버튼 클릭에 따른 함수 실행
$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendMessage());
});

// 쿠키 가져오기
function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
};
