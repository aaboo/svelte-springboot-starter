    const DEFAULT_SIZE = 1.3;
const ICON = function(){}
ICON.prototype = {
    /****************************
     * 회사로고
     ***************************/
    logo: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" version="1.1" viewBox="0 0 35 35">
            <g transform="translate(-81.692 -108.86)">
                <g transform="matrix(.44304 0 0 .44304 -188.01 -308)">
                    <path transform="matrix(1.3333 0 0 -1.3333 0 1122.5)" d="m515.81 76.964h-59.251v59.25h59.251z" clip-path="url(#clipPath472-3)" fill="#2350a9"/>
                    <path transform="matrix(1.3333 0 0 -1.3333 670.33 980.77)" d="m0 0-14.382 0.668-0.641 13.977h-0.026l-0.652-13.977-14.367-0.668v-0.023l14.367-0.616 0.652-14.779h0.026l0.641 14.779 14.382 0.616z" clip-path="url(#clipPath474-9)" fill="#f79528"/>
                    <g fill="#fff">
                        <path transform="matrix(1.3333 0 0 -1.3333 644.2 952.3)" d="m0 0v-0.631h-0.312c-0.892 0-1.876-0.178-1.876-2.682v-9.587c0-0.741 0.019-1.475 0.12-2.133h-0.796l-10.852 12.099v-8.681c0-1.84 0.354-2.471 1.977-2.471h0.36v-0.621h-5.588v0.621h0.293c1.454 0 1.896 0.992 1.896 2.678v8.875c0 1.181-0.968 1.902-1.917 1.902h-0.272v0.631h4.297l9.384-10.566 0.043 0.048v7.901c0 1.728-1.205 1.986-1.842 1.986h-0.459v0.631z" clip-path="url(#clipPath476-0)"/>
                        <path transform="matrix(1.3333 0 0 -1.3333 663.63 971.91)" d="m0 0v0.621h0.301c0.758 0 1.559 0.111 1.559 1.207v11.037c0 1.104-0.801 1.211-1.559 1.211h-0.301v0.631h6.434v-0.631h-0.291c-0.762 0-1.559-0.107-1.559-1.211v-11.037c0-1.096 0.797-1.207 1.559-1.207h0.291v-0.621z" clip-path="url(#clipPath478-9)"/>
                        <path transform="matrix(1.3333 0 0 -1.3333 640.21 1008.3)" d="m0 0c-1.212-0.533-2.883-0.949-4.824-0.949-4.745 0-8.261 2.594-8.261 7.883 0 4.718 3.819 7.457 8.156 7.457 2.469 0 3.267-0.76 5.246-1.051l0.171-3.524-0.634-0.082c-0.586 2.049-1.556 3.727-4.72 3.727-3.45 0-5.273-2.717-5.273-6.406 0-3.514 1.802-7.076 5.778-7.076 2.254 0 3.75 1.404 4.296 3.517l0.598-0.082z" clip-path="url(#clipPath480-6)"/>
                        <path transform="matrix(1.3333 0 0 -1.3333 659.55 1009.1)" d="m0 0v0.635h0.304c0.762 0 1.555 0.103 1.555 1.201v11.041c0 1.098-0.793 1.203-1.555 1.203h-0.304v0.629h10.658c-0.049-0.904-0.024-2.291 0.038-3.223l-0.635-0.172c-0.106 1.377-0.357 2.471-2.573 2.471h-2.929v-5.525h2.507c1.263 0 1.539 0.715 1.667 1.857h0.63v-4.865l-0.63-0.137c-0.128 1.27-0.186 2.088-1.645 2.088h-2.529v-4.906c0-1.375 1.22-1.375 2.57-1.375 2.535 0 3.645 0.17 4.279 2.58l0.596-0.148c-0.279-1.118-0.525-2.239-0.711-3.354z" clip-path="url(#clipPath482-7)"/>
                    </g>
                </g>
            </g>
        </svg>
    `
    /****************************
     * 메뉴관련
     ***************************/
    //배치관리
    , jobCalendar: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M7 11h2v2H7zm0 4h2v2H7zm4-4h2v2h-2zm0 4h2v2h-2zm4-4h2v2h-2zm0 4h2v2h-2z"/>
            <path fill="currentColor" d="M5 22h14c1.103 0 2-.897 2-2V6c0-1.103-.897-2-2-2h-2V2h-2v2H9V2H7v2H5c-1.103 0-2 .897-2 2v14c0 1.103.897 2 2 2M19 8l.001 12H5V8z"/>
        </svg>
    `
    //배치작업
    , jobProc: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M14 9h-4a1 1 0 0 0-1 1v4a1 1 0 0 0 1 1h4a1 1 0 0 0 1-1v-4a1 1 0 0 0-1-1m-1 4h-2v-2h2Zm8 0a1 1 0 0 0 0-2h-2V9h2a1 1 0 0 0 0-2h-2.18A3 3 0 0 0 17 5.18V3a1 1 0 0 0-2 0v2h-2V3a1 1 0 0 0-2 0v2H9V3a1 1 0 0 0-2 0v2.18A3 3 0 0 0 5.18 7H3a1 1 0 0 0 0 2h2v2H3a1 1 0 0 0 0 2h2v2H3a1 1 0 0 0 0 2h2.18A3 3 0 0 0 7 18.82V21a1 1 0 0 0 2 0v-2h2v2a1 1 0 0 0 2 0v-2h2v2a1 1 0 0 0 2 0v-2.18A3 3 0 0 0 18.82 17H21a1 1 0 0 0 0-2h-2v-2Zm-4 3a1 1 0 0 1-1 1H8a1 1 0 0 1-1-1V8a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1Z"/>
        </svg>
    `
    //배치결과
    , batchResult: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M6 6H4v2h2zm14 0H8v2h12zM4 11h2v2H4zm16 0H8v2h12zM4 16h2v2H4zm16 0H8v2h12z"/>
        </svg>
    `
    //DAEMON
    , daemon: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M15.5 9a1.492 1.492 0 0 0-1.064.427A1.522 1.522 0 0 0 14 10.5a1.468 1.468 0 0 0 .436 1.05A1.511 1.511 0 0 0 15.5 12a1.496 1.496 0 0 0 1.058-.45A1.474 1.474 0 0 0 17 10.5a1.527 1.527 0 0 0-.442-1.073A1.476 1.476 0 0 0 15.5 9m-7 0a1.499 1.499 0 1 0 1.06.439A1.496 1.496 0 0 0 8.5 9"/>
            <path fill="currentColor" d="M18 2a16.282 16.282 0 0 1 .51 2.145a5.04 5.04 0 0 1-.083 1.56a8.987 8.987 0 0 0-12.853 0a4.922 4.922 0 0 1-.063-1.56A4.908 4.908 0 0 1 6 2a9.821 9.821 0 0 0-2.546 2.645a4.996 4.996 0 0 0-.128 3.84a4.851 4.851 0 0 0 .2.455l.012.02a9 9 0 1 0 16.923-.003l.01-.017a4.852 4.852 0 0 0 .2-.455a5.053 5.053 0 0 0-.104-3.84A9.599 9.599 0 0 0 18 2m-6 17a7 7 0 1 1 7-7a7.008 7.008 0 0 1-7 7"/>
            <path fill="currentColor" d="M7 14a5.493 5.493 0 0 0 5 3a5.493 5.493 0 0 0 5-3Z"/>
        </svg>
    `
    //매입처리
    , purchase: (size=DEFAULT_SIZE)=>`        
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="m14 10l-3 3m9.288-9.969a.535.535 0 0 1 .68.681l-5.924 16.93a.535.535 0 0 1-.994.04l-3.219-7.242a.535.535 0 0 0-.271-.271l-7.242-3.22a.535.535 0 0 1 .04-.993z"/>
        </svg>
    `
    //파일송수신
    , filetran: (size=DEFAULT_SIZE)=>`        
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <g transform="rotate(90 12 12)">
                <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 4v16l-6-5.5M14 20V4l6 5.5"/>
            </g>
        </svg>
    `
    /****************************
     * 일반
     ***************************/
    //얇은X
    , close: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 32 32">
            <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2 30L30 2m0 28L2 2"/>
        </svg>
    `
    //두꺼운 X
    , cross: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 16 16">
            <path fill="currentColor" d="M15.854 12.854L11 8l4.854-4.854a.503.503 0 0 0 0-.707L13.561.146a.499.499 0 0 0-.707 0L8 5L3.146.146a.5.5 0 0 0-.707 0L.146 2.439a.499.499 0 0 0 0 .707L5 8L.146 12.854a.5.5 0 0 0 0 .707l2.293 2.293a.499.499 0 0 0 .707 0L8 11l4.854 4.854a.5.5 0 0 0 .707 0l2.293-2.293a.499.499 0 0 0 0-.707z"/>
        </svg>
    `
    //체크 모양
    , check: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 8 8">
            <path fill="currentColor" d="m6.41 1l-.69.72L2.94 4.5l-.81-.78L1.41 3L0 4.41l.72.72l1.5 1.5l.69.72l.72-.72l3.5-3.5l.72-.72L6.41 1z"/>
        </svg>
    `
    //말풍선
    , message: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 20 20">
            <g transform="translate(20 0) scale(-1 1)">
                <path fill="currentColor" d="M18 6v7c0 1.1-.9 2-2 2h-4v3l-4-3H4c-1.101 0-2-.9-2-2V6c0-1.1.899-2 2-2h12c1.1 0 2 .9 2 2z"/>
            </g>
        </svg>
    `
    //동그라미 가운데 사선 (/)
    , notAllowed: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 512 512">
            <path fill="currentColor" d="M256 0C114.6 0 0 114.6 0 256s114.6 256 256 256s256-114.6 256-256S397.4 0 256 0zM64 256c0-106.1 86-192 192-192c42.1 0 81 13.7 112.6 36.7L100.7 368.6C77.7 337 64 298.1 64 256zm192 192c-42.1 0-81-13.7-112.6-36.7l267.9-267.9c23 31.7 36.7 70.5 36.7 112.6c0 106.1-86 192-192 192z"/>
        </svg>
    `
    // 왼쪽화살표1 <
    , left1: (size=DEFAULT_SIZE)=>`        
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="m10.8 12l3.9 3.9q.275.275.275.7t-.275.7t-.7.275t-.7-.275l-4.6-4.6q-.15-.15-.212-.325T8.425 12t.063-.375t.212-.325l4.6-4.6q.275-.275.7-.275t.7.275t.275.7t-.275.7z"/>
        </svg>
    `
    // 왼쪽화살표2 <
    , left2: (size=DEFAULT_SIZE)=>`        
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="m7.825 12l3.875 3.9q.275.275.288.688t-.288.712q-.275.275-.7.275t-.7-.275l-4.6-4.6q-.15-.15-.213-.325T5.426 12t.063-.375t.212-.325l4.6-4.6q.275-.275.688-.287t.712.287q.275.275.275.7t-.275.7zm6.6 0l3.875 3.9q.275.275.288.688t-.288.712q-.275.275-.7.275t-.7-.275l-4.6-4.6q-.15-.15-.213-.325T12.026 12t.063-.375t.212-.325l4.6-4.6q.275-.275.688-.287t.712.287q.275.275.275.7t-.275.7z"/>
        </svg>
    `
    //문으로 들어오는 화살표
    , login: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
                <path d="M9 8V6a2 2 0 0 1 2-2h7a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2h-7a2 2 0 0 1-2-2v-2"/>
                <path d="M3 12h13l-3-3m0 6l3-3"/>
            </g>
        </svg>
    `
    //문에서 왼쪽으로 나가는 화살표
    , logout: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
                <path d="M10 8V6a2 2 0 0 1 2-2h7a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2h-7a2 2 0 0 1-2-2v-2"/>
                <path d="M15 12H3l3-3m0 6l-3-3"/>
            </g>
        </svg>
    `
    //핀
    , pin: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 12 12">
            <path fill="currentColor" d="M8.052 1.436a1.5 1.5 0 0 0-2.38.347L4.145 4.608l-2.33.928a.5.5 0 0 0-.169.818l1.647 1.647l-2.146 2.146l-.147.854l.854-.147L4 8.708l1.646 1.646a.5.5 0 0 0 .818-.168l.933-2.332l2.821-1.526a1.5 1.5 0 0 0 .347-2.38L8.052 1.436Zm-1.5.822a.5.5 0 0 1 .793-.115l2.513 2.513a.5.5 0 0 1-.116.793L6.762 7.06a.5.5 0 0 0-.226.254L5.817 9.11L2.891 6.184l1.793-.715a.5.5 0 0 0 .254-.226l1.614-2.985Z"/>
        </svg>
    `
    // 굵은 오른쪽화살표1 >
    , rightBold: (size=DEFAULT_SIZE)=>`        
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 8 8">
            <path fill="currentColor" d="M2.5 0L1 1.5L3.5 4L1 6.5L2.5 8l4-4l-4-4z"/>
        </svg>
    `
    // 오른쪽화살표1 >
    , right1: (size=DEFAULT_SIZE)=>`        
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M12.6 12L8.7 8.1q-.275-.275-.275-.7t.275-.7t.7-.275t.7.275l4.6 4.6q.15.15.213.325t.062.375t-.062.375t-.213.325l-4.6 4.6q-.275.275-.7.275t-.7-.275t-.275-.7t.275-.7z"/>
        </svg>
    `
    // 오른쪽화살표2 >>
    , right2: (size=DEFAULT_SIZE)=>`        
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M9.575 12L5.7 8.1q-.275-.275-.288-.687T5.7 6.7q.275-.275.7-.275t.7.275l4.6 4.6q.15.15.213.325t.062.375t-.062.375t-.213.325l-4.6 4.6q-.275.275-.687.288T5.7 17.3q-.275-.275-.275-.7t.275-.7zm6.6 0L12.3 8.1q-.275-.275-.288-.687T12.3 6.7q.275-.275.7-.275t.7.275l4.6 4.6q.15.15.213.325t.062.375t-.062.375t-.213.325l-4.6 4.6q-.275.275-.687.288T12.3 17.3q-.275-.275-.275-.7t.275-.7z"/>
        </svg>
    `
    //돋보기
    , search: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="m21 19l-5.154-5.154a7 7 0 1 0-2 2L19 21l2-2zM5 10c0-2.757 2.243-5 5-5s5 2.243 5 5s-2.243 5-5 5s-5-2.243-5-5z"/>
        </svg>
    `
    //세점 중에서 처음과 끝점 연결됨
    , skip: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M21 12c0-4-2.5-7.3-6-8.5c-.3-1.4-1.5-2.5-3-2.5c-1.7 0-3 1.3-3 3s1.3 3 3 3c1.1 0 2.1-.6 2.6-1.5C17.2 6.5 19 9 19 12c0 2.9-1.8 5.5-4.4 6.5c-.5-.9-1.5-1.5-2.6-1.5c-1.7 0-3 1.3-3 3s1.3 3 3 3c1.5 0 2.7-1.1 3-2.5c3.5-1.2 6-4.6 6-8.5m-9-7c-.6 0-1-.4-1-1s.4-1 1-1s1 .4 1 1s-.4 1-1 1m0 16c-.6 0-1-.4-1-1s.4-1 1-1s1 .4 1 1s-.4 1-1 1m0-12c1.7 0 3 1.3 3 3s-1.3 3-3 3s-3-1.3-3-3s1.3-3 3-3Z"/>
        </svg>
    `
    //아래 방향 ∨
    , toDown: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <g transform="rotate(180 12 12)">
                <g fill="none" fill-rule="evenodd">
                    <path d="M24 0v24H0V0h24ZM12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035c-.01-.004-.019-.001-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427c-.002-.01-.009-.017-.017-.018Zm.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093c.012.004.023 0 .029-.008l.004-.014l-.034-.614c-.003-.012-.01-.02-.02-.022Zm-.715.002a.023.023 0 0 0-.027.006l-.006.014l-.034.614c0 .012.007.02.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01l-.184-.092Z"/>
                    <path fill="currentColor" d="M10.94 7.94a1.5 1.5 0 0 1 2.12 0l5.658 5.656a1.5 1.5 0 1 1-2.122 2.121L12 11.121l-4.596 4.596a1.5 1.5 0 1 1-2.122-2.12l5.657-5.658Z"/>
                </g>
            </g>
        </svg>
    `
    //위 방향 ∧
    , toUp: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <g fill="none" fill-rule="evenodd">
                <path d="M24 0v24H0V0h24ZM12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035c-.01-.004-.019-.001-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427c-.002-.01-.009-.017-.017-.018Zm.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093c.012.004.023 0 .029-.008l.004-.014l-.034-.614c-.003-.012-.01-.02-.02-.022Zm-.715.002a.023.023 0 0 0-.027.006l-.006.014l-.034.614c0 .012.007.02.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01l-.184-.092Z"/>
                <path fill="currentColor" d="M10.94 7.94a1.5 1.5 0 0 1 2.12 0l5.658 5.656a1.5 1.5 0 1 1-2.122 2.121L12 11.121l-4.596 4.596a1.5 1.5 0 1 1-2.122-2.12l5.657-5.658Z"/>
            </g>
        </svg>
    `
    //동그라미 안에 느낌표 (!)
    , warn: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 20 20">
            <path fill="currentColor" d="M10 2c4.42 0 8 3.58 8 8s-3.58 8-8 8s-8-3.58-8-8s3.58-8 8-8zm1.13 9.38l.35-6.46H8.52l.35 6.46h2.26zm-.09 3.36c.24-.23.37-.55.37-.96c0-.42-.12-.74-.36-.97s-.59-.35-1.06-.35s-.82.12-1.07.35s-.37.55-.37.97c0 .41.13.73.38.96c.26.23.61.34 1.06.34s.8-.11 1.05-.34z"/>
        </svg>
    `
    , gpsOn: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M12 8c-2.21 0-4 1.79-4 4s1.79 4 4 4s4-1.79 4-4s-1.79-4-4-4m8.94 3A8.994 8.994 0 0 0 13 3.06V1h-2v2.06A8.994 8.994 0 0 0 3.06 11H1v2h2.06A8.994 8.994 0 0 0 11 20.94V23h2v-2.06A8.994 8.994 0 0 0 20.94 13H23v-2zM12 19c-3.87 0-7-3.13-7-7s3.13-7 7-7s7 3.13 7 7s-3.13 7-7 7"/>
        </svg>
    `
    , gpsOff: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M20.94 11A8.994 8.994 0 0 0 13 3.06V1h-2v2.06c-1.13.12-2.19.46-3.16.97l1.5 1.5A6.995 6.995 0 0 1 19 12c0 .94-.19 1.84-.52 2.65l1.5 1.5c.5-.96.84-2.02.97-3.15H23v-2zM3 4.27l2.04 2.04A8.9 8.9 0 0 0 3.06 11H1v2h2.06A8.994 8.994 0 0 0 11 20.94V23h2v-2.06c1.77-.2 3.38-.91 4.69-1.98L19.73 21L21 19.73L4.27 3zm13.27 13.27a6.995 6.995 0 0 1-9.81-9.81z"/>
        </svg>
    `
    /****************************
     * 에니메이션
     ***************************/    
    //[에니메이션] 시계 분침 초침 빙글 빙글
    , clock: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <path fill="currentColor" d="M12,1A11,11,0,1,0,23,12,11,11,0,0,0,12,1Zm0,20a9,9,0,1,1,9-9A9,9,0,0,1,12,21Z"/>
            <rect width="2" height="7" x="11" y="6" fill="currentColor" rx="1">
                <animateTransform attributeName="transform" dur="60s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/>
            </rect>
            <rect width="2" height="9" x="11" y="11" fill="currentColor" rx="1">
                <animateTransform attributeName="transform" dur="10s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/>
            </rect>
        </svg>
    `
    //[에니메이션] 로딩 3개 네모 에니메이션
    , loading: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <rect width="10" height="10" x="1" y="1" fill="currentColor" rx="1">
                <animate id="svgSpinnersBlocksShuffle30" fill="freeze" attributeName="x" begin="0;svgSpinnersBlocksShuffle3b.end" dur="0.2s" values="1;13"/>
                <animate id="svgSpinnersBlocksShuffle31" fill="freeze" attributeName="y" begin="svgSpinnersBlocksShuffle38.end" dur="0.2s" values="1;13"/>
                <animate id="svgSpinnersBlocksShuffle32" fill="freeze" attributeName="x" begin="svgSpinnersBlocksShuffle39.end" dur="0.2s" values="13;1"/>
                <animate id="svgSpinnersBlocksShuffle33" fill="freeze" attributeName="y" begin="svgSpinnersBlocksShuffle3a.end" dur="0.2s" values="13;1"/>
            </rect>
            <rect width="10" height="10" x="1" y="13" fill="currentColor" rx="1">
                <animate id="svgSpinnersBlocksShuffle34" fill="freeze" attributeName="y" begin="svgSpinnersBlocksShuffle30.end" dur="0.2s" values="13;1"/>
                <animate id="svgSpinnersBlocksShuffle35" fill="freeze" attributeName="x" begin="svgSpinnersBlocksShuffle31.end" dur="0.2s" values="1;13"/>
                <animate id="svgSpinnersBlocksShuffle36" fill="freeze" attributeName="y" begin="svgSpinnersBlocksShuffle32.end" dur="0.2s" values="1;13"/>
                <animate id="svgSpinnersBlocksShuffle37" fill="freeze" attributeName="x" begin="svgSpinnersBlocksShuffle33.end" dur="0.2s" values="13;1"/>
            </rect>
            <rect width="10" height="10" x="13" y="13" fill="currentColor" rx="1">
                <animate id="svgSpinnersBlocksShuffle38" fill="freeze" attributeName="x" begin="svgSpinnersBlocksShuffle34.end" dur="0.2s" values="13;1"/>
                <animate id="svgSpinnersBlocksShuffle39" fill="freeze" attributeName="y" begin="svgSpinnersBlocksShuffle35.end" dur="0.2s" values="13;1"/>
                <animate id="svgSpinnersBlocksShuffle3a" fill="freeze" attributeName="x" begin="svgSpinnersBlocksShuffle36.end" dur="0.2s" values="1;13"/>
                <animate id="svgSpinnersBlocksShuffle3b" fill="freeze" attributeName="y" begin="svgSpinnersBlocksShuffle37.end" dur="0.2s" values="1;13"/>
            </rect>
        </svg>
    `
    //[에니메이션] 로딩 - 기본로딩
    , loading2: (size=DEFAULT_SIZE)=>`
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <g stroke="currentColor">
                <circle cx="12" cy="12" r="9.5" fill="none" stroke-linecap="round" stroke-width="3">
                    <animate attributeName="stroke-dasharray" calcMode="spline" dur="1.5s" keySplines="0.42,0,0.58,1;0.42,0,0.58,1;0.42,0,0.58,1" keyTimes="0;0.475;0.95;1" repeatCount="indefinite" values="0 150;42 150;42 150;42 150"/>
                    <animate attributeName="stroke-dashoffset" calcMode="spline" dur="1.5s" keySplines="0.42,0,0.58,1;0.42,0,0.58,1;0.42,0,0.58,1" keyTimes="0;0.475;0.95;1" repeatCount="indefinite" values="0;-16;-59;-59"/>
                </circle>
                <animateTransform attributeName="transform" dur="2s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/>
            </g>
        </svg>
    `
    //[에니메이션] 8개 작은 점이 빙글빙글 도는 에니메이션
    , working: (size=DEFAULT_SIZE)=>`    
        <svg width="${size}rem" height="${size}rem" viewBox="0 0 24 24">
            <g>
                <circle cx="3" cy="12" r="2" fill="currentColor"/>
                <circle cx="21" cy="12" r="2" fill="currentColor"/>
                <circle cx="12" cy="21" r="2" fill="currentColor"/>
                <circle cx="12" cy="3" r="2" fill="currentColor"/>
                <circle cx="5.64" cy="5.64" r="2" fill="currentColor"/>
                <circle cx="18.36" cy="18.36" r="2" fill="currentColor"/>
                <circle cx="5.64" cy="18.36" r="2" fill="currentColor"/>
                <circle cx="18.36" cy="5.64" r="2" fill="currentColor"/>
                <animateTransform attributeName="transform" dur="1.5s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/>
            </g>
        </svg>
    `
}

const icon = new ICON();

export default icon;