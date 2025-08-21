import jqr from 'jquery';
import ses from '$lib/ses';
import icon from '$lib/icon';

const App = function(){
    this.name = "svelteSpringbootStarter";//★★★쿠키, 세션스토리지 명으로 사용됨
    this.port_backend = 8080; //★★★Front-end 서비스 포트(npm run dev에서만 사용됨)
    this.port_frontend_dev = 8081; //★★★Back-end WAS 포트 (npm run dev에서 back-end와 통신할 때 사용
    //vite.config.js server.proxy 참조
    //npm run dev에서 back-end와 통신하기 위해 /back-end를 강제로 붙여준다.
    //console.log(location, location.port=="28082");
    if(location.port==this.port_frontend_dev.toString()){
        //npm run dev 사용
        this.PATH = "/back-end";
        this.WEBSOCKET = `ws://${location.hostname}:${this.port_backend}/socket`
    }else{
        //npm run build 사용
        this.PATH = "";
        this.WEBSOCKET = `ws://${location.host}/socket`
    }
}
App.prototype = {
    authCheck: async function(){        
        const authCheck = await fetch(this.PATH+"/auth/check");
        return await this.getResult(authCheck);
    }
    , getResult: async function(res){
        let result;
        await new Promise(async (resolve, reject)=>{
            result = await res.text();
            try{
                result = JSON.parse(result);
                if(result.data) result.data = JSON.parse(result.data);
            }catch(e){
                result = result;
                //console.log(e);
            }            
            return resolve();
        });
        
        return result;
    }
    , fetch: async function(...param){
        let pr0 = param[0]??""
            , pr1 = param[1]??{};
        if(pr0==="") return "";
        
        //Back-end 주소 강제 변경
        pr0 = this.PATH + pr0;

        //body
        pr1["body"] = Object.assign({}, pr1);
        delete pr1["body"]["method"];
        delete pr1["body"]["headers"];
        for(let k in pr1["body"]){
            delete pr1[k];
        }
        //console.log(pr0, pr1["body"]);
        pr1["body"] = encodeURIComponent(JSON.stringify(pr1["body"]));
        //pr1["body"] = pr1["body"];
        //method
        pr1["method"] = pr1["method"]?pr1["method"]:"POST";
        //headers        
        pr1["headers"] = pr1["headers"]
            ? pr1["headers"]["Content-Type"]
                ? pr1["headers"]["Content-Type"]
                :'application/x-www-form-urlencoded'
            : {'Content-Type': 'application/x-www-form-urlencoded'}

        //로그인 상태 확인        
        const SS = JSON.parse(sessionStorage.getItem(app.name));        
        if(pr0!=(this.PATH + "/auth/login") && !SS){
            //console.log("app.fetch > authCheck", authCheck);
            //alert("로그인이 필요합니다(1)");
            location.href="/login";        
        }else{
            //정상적인 auth 접속은 authCheck를 하지 않는다.
            if(pr0.indexOf(this.PATH+"/auth")>-1){
                const response = await fetch(pr0, pr1);
                return await this.getResult(response);
            }
            const authCheck = await this.authCheck();
            //console.log(authCheck);
            if(!authCheck){
                alert("시스템에 문제가 있습니다.");
                location.href="/login";
            }else if(!authCheck.data){
                alert("로그인이 필요합니다.(2)");
                location.href="/login";
            }else{
                
                const response = await fetch(pr0, pr1);
                //console.log(response);
                if(!response.ok){
                    throw new Error(`통신에 문제가 있습니다: ${response.status}`);
                    this.modal.close();
                }else if(response.redirected){
                    location.href= response.url;
                    alert('로그인이 필요합니다(3)');
                    throw new Error(`로그인이 필요합니다(3)`);
                }
                await this.sessionReset();
                return await this.getResult(response);
            }
        }
        
    }
    //로그인 시간 연장
    , sessionReset: async ()=>{
        await fetch(app.PATH+"/auth/reset")
        .then(()=>{
            ses.resetTimer();
        });
    }
    //쿠키 불러오기
    , getCookie: (name)=>{
        const cookies = document.cookie.split(';');
        //console.log(cookies);
        for(let i in cookies){
            const cookie = cookies[i].trim();
            if(cookie.startsWith(name+'=')){
                return cookie.substring(name.length+1);
            }
        }
        return null;
    }
    //쿠키 저장하기
    , setCookie: (name, value, maxAge=1, path="/", domain="")=>{
        const dateLimit = new Date();
        dateLimit.setTime(dateLimit.getTime() + (1000*60*60*24*maxAge));
        const expires = `expires=${dateLimit.toUTCString()}`;
        const cookie = `${name}=${value}; ${expires}; path=${path}; domain=${domain}`;
        document.cookie = cookie;
    }
    , go: async function(url="/"){
        //로그인 상태 확인
        const authCheck = await this.authCheck();
        if(!authCheck.data){
            console.log("authCheck", authCheck);
            location.href="/login";
            return;
        }
        location.href=url;
    }    
    //메뉴이동 로그 기록
    , hitMenu: function(pathname){
        if(pathname!==""){
            app.fetch("/user/hitMenu", {pathname:pathname});
        }
    }
    //로그아웃
    , logout: async function(){
        fetch(this.PATH+"/auth/logout"); 
        this.resetSessionStorageSearch();////조회날짜 초기화
    }
    //로그인 재시도시 로그오프
    , logoff: async function(){
        fetch(this.PATH+"/auth/logoff");
        this.resetSessionStorageSearch();////조회날짜 초기화
    }
    //조회날짜 초기화 
    , resetSessionStorageSearch: function(){
        const SS = JSON.parse(sessionStorage.getItem(app.name));
        if(SS){
            SS["search"] = {};
            sessionStorage.setItem(app.name, JSON.stringify(SS));  
        }
    }
    //세션스토리지 작성
    , ss: {
        structure: {
            menu:{}
            , search:{}
            , data:{}
        }
        , setSearch: function(param){
            const SS = JSON.parse(sessionStorage.getItem(app.name));
            if(SS){
                SS["search"][SS.menu.currentMenuID] = {...param};
                sessionStorage.setItem(app.name, JSON.stringify(SS));  
            }
        }
        , getSearch: function(_cid){
            const SS = JSON.parse(sessionStorage.getItem(app.name));
            if(SS) return SS["search"][_cid??SS.menu.currentMenuID];
        }
        , setData: function(param){
            const SS = JSON.parse(sessionStorage.getItem(app.name));
            if(SS){
                SS["data"][SS.menu.currentMenuID] = {...param};
                sessionStorage.setItem(app.name, JSON.stringify(SS));  
            }
        }
        , getData: function(_cid){
            const SS = JSON.parse(sessionStorage.getItem(app.name));
            if(SS) return SS["data"][_cid??SS.menu.currentMenuID];
        }
    }
    //모달
    , modal: {
        open: function(req={base:"base", message:"Loading"}){
            //const modal = document.createElement("div");
            if(!req.base) req.base = "base";
            let dom = jqr("div#frame");
            if(req.base=="body") dom = document.body
            else dom = jqr(`div#${req.base}`);

            dom.after(`
                <div id="modal" style="
                    position:fixed;
                    display:flex;
                    flex-direction:column;
                    justify-content:center;
                    align-items:center;
                    top:0;
                    left:0;
                    width:100vw;
                    height:100vh;
                    background-color:rgba(0,0,0,0.3);
                    z-index:99999999999999;
                    text-shadow:0 0px 4px rgb(0 0 0/50%);
                ">
                    <span style="
                        display:flex;
                        flex-direction:column;
                        justify-content:center;
                        align-items:center;
                        color:white;

                    ">
                        ${icon.loading2(1.8)}
                        <span style="
                            margin:0.5rem 0 5rem 0;
                            font-size:0.8rem;
                        ">
                            ${req.message}
                        </span>
                    </span>
                </div>
            `);
        }
        , close: ()=>{
            jqr('div#modal').remove();
        }
    }
    //case가 true 이면 
    , alertFocus(check, message, target){
        if(check){
            alert(message);
            target.focus();
            return true;
        }
        return false;
    }

}

//기본CSS - js에서 사용됨
App.prototype.css = {
    contentMinWidth: 300
}

const app = new App();
//window.app = app;
window.jqr = jqr;
export default app;