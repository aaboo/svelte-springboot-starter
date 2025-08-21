<script>
const ID = "popupProcDetail";

import { onMount, createEventDispatcher } from 'svelte';
import app from '$lib/app';
import Popup from '../Popup.svelte';

const dispatch = createEventDispatcher();

export let proc = {};
export let from = "";

onMount(()=>{
    NV.dom.procPopupComment?.focus();    
});

const NV = {
    init: function(){
        const ds = this;        
    }
    , dom: {
        procPopupComment: null //PROC 팝업창 상세 팝업 작업자 한 줄 코멘트
    }
    //Action
    , action: {
        //스킵처리(C-기존 procstatus)
        btnProcC: function(ev){
            ev.preventDefault();
            const param = {
                workdate: proc.workdate
                , proccode: proc.proccodeorg
                , createid: proc.createid
                , comment: NV.dom.procPopupComment.value
            };
            //console.log("btnProcC", param.comment, param);
            if(["C","N"].indexOf(proc.procstatus)<0){
                if(app.alertFocus(!param.comment, "코멘트를 작성해 주세요", NV.dom.procPopupComment)) return;
                app.modal.open();
                app.fetch("/jobproc/updateProcC", param).then(async (response)=>{
                    //console.log(response);
                    if(response.data>0){                        
                        alert("스킵 처리 되었습니다.");
                        dispatch("close");
                    }else alert("스킵처리에 실패하였습니다.\n확인이 필요합니다.");                    
                }).finally(()=>{
                    app.modal.close();
                });
            }else{
                alert("스킵대상이 아닙니다.(실행상태[X,Q,R]만 허용)");
            }
        }
        //자료없음(C-N)
        , btnProcN: function(ev){
            ev.preventDefault();
            const param = {
                workdate: proc.workdate
                , proccode: proc.proccodeorg
                , createid: proc.createid
                , comment: NV.dom.procPopupComment.value
            };
            //console.log("btnProcN", proc);
            if(proc.procstatus=="E"){
                if(app.alertFocus(!param.comment, "코멘트를 작성해 주세요", NV.dom.procPopupComment)) return;
                app.modal.open();
                app.fetch("/jobproc/updateProcN", param).then(async (response)=>{
                    //console.log(response);
                    if(response.data>0){      
                        alert("자료없음 처리 되었습니다.");
                        dispatch("close");
                    }else alert("자료없음 처리에 실패하였습니다.\n확인이 필요합니다.");                    
                }).finally(()=>{
                    app.modal.close();
                });
            }else{
				alert("자료없음 처리를 할 수 없습니다.(실행상태[E]만 허용)");
			}
        }
        //재실행
        , btnProcQ: function(ev){
            ev.preventDefault();
            const param = {
                workdate: proc.workdate
                , proccode: proc.proccodeorg
                , createid: proc.createid
            };
            //console.log("btnProcQ", proc, param.procstatus=="C" && param.reworkflag=="O");
            if(proc.procstatus=="C" && proc.reworkflag=='O'){
                if(!confirm("재실행 하시겠습니까?")) return;
                app.modal.open();
                app.fetch("/jobproc/insertProcQ", param).then(async (response)=>{
                    //console.log(response);
                    if(response.data>0){
                        alert("재실행 처리 되었습니다.");
                        dispatch("close");
                    }else alert("재실행에 실패하였습니다.\n확인이 필요합니다.");                    
                }).finally(()=>{
                    app.modal.close();
                });
            }else{
				alert("재실행 대상이 아닙니다.\n(실행상태[C]+재작업가능여부[O]만 허용)");
			}
        }
    }
}
</script>

<!-- PROC 상세 팝업 -->
<Popup on:close={()=>{dispatch("close")}}
    title={`PROC 상세정보(${proc.proccodeorg})`}
    width={800}
    height={900}
    showFooter={true}
>
    <div slot="body">
        <div class="tableInPopupWrapper">
            <table class="tableInPopup" border="1">
                <colgroup>
                    <col width="100">
                    <col width="200">
                    <col width="100">
                    <col width="200">
                </colgroup>
                <tbody>
                    <tr>
                        <th>프로세스명칭</th>
                        <td colspan="3" title="procname">{proc.procname}</td>
                    </tr>
                    <tr>
                        <th>실행주기</th>
                        <td colspan="3" title="typekindnm">{proc.typekindnm}</td>
                    </tr>
                    <tr>
                        <th>프로세스파일명</th>
                        <td title="procfile">{proc.procfile}</td>
                        <th>대상서버</th>
                        <td title="procsvr">{proc.procsvrnm}({proc.procsvr})</td>
                    </tr>
                    <tr>
                        <th>실행명령어</th>
                        <td colspan="3" title="proccmd">{proc.proccmd}</td>
                    </tr>
                    <tr>
                        <th>매개변수</th>
                        <td title="procparam">{proc.procparam}</td>
                        <th>작업기준일자</th>
                        <td title="inputdate">{proc.inputdate}</td>
                    </tr>
                    <tr>
                        <th>실행명령문</th>
                        <td colspan="3" title="procrun">{proc.procrun}</td>
                    </tr>
                    <tr>
                        <th>재작업가능여부</th>
                        <td title="reworkflag">{proc.reworkflag}</td>
                        <th>작업소요시간</th>
                        <td title="worktime">{proc.worktime}</td>
                    </tr>
                    <tr>
                        <th>자동수동구분</th>
                        <td title="runtype">{proc.runtypenm}({proc.runtype})</td>
                        <th>작업순서</th>
                        <td title="procorder">{(proc.procorder??"").money()}</td>
                    </tr>
                    <tr>
                        <th>SKIP 가능여부</th>
                        <td title="skipflag">{proc.skipflag}</td>
                        <th>선행프로세스</th>
                        <td title="preproc">{proc.preproc}</td>
                    </tr>
                    <tr>
                        <th>작업지시사항</th>
                        <td colspan="3" title="proclistmemo">{proc.proclistmemo}</td>
                    </tr>
                </tbody>
            </table>
            <div style="height:0.5rem"/>
            <table class="tableInPopup" border="1">
                <colgroup>
                    <col width="100">
                    <col width="200">
                    <col width="100">
                    <col width="200">
                </colgroup>
                <tbody>
                    <tr>
                        <th>배치업무코드</th>
                        <td title="jobcode">{proc.jobcode}</td>
                        <th>프로세스코드</th>
                        <td title="proccodeorg">{proc.proccodeorg}</td>
                    </tr>
                    <tr>
                        <th>자료등록자ID</th>
                        <td title="userid">{proc.userid}</td>
                        <th>자료등록일시</th>
                        <td tiele="created">{proc.created}</td>
                    </tr>
                    <tr>
                        <th>작업확인자ID</th>
                        <td title="chkuser">{proc.chkuser}</td>
                        <th>작업확인일시</th>
                        <td title="chktime">{proc.chktime}</td>
                    </tr>
                    <tr>
                        <th>실행시작시각</th>
                        <td title="starttime">{proc.starttime}</td>
                        <th>실행완료시각</th>
                        <td title="endtime">{proc.endtime}</td>
                    </tr>
                    <tr>
                        <th>파일수신상태</th>
                        <td title="starttime">{proc.filestatusnm}
                            {#if proc.filestatus!=""}
                                ({proc.filestatus})
                            {/if}
                        </td>
                        <th>파일수신시간</th>
                        <td title="endtime">{proc.recvtime}</td>
                    </tr>
                </tbody>
            </table>
            <div style="height:0.5rem"/>
            <table class="tableInPopup" border="1">
                <colgroup>
                    <col width="100">
                    <col width="200">
                    <col width="100">
                    <col width="200">
                </colgroup>
                <tbody>
                    <tr>
                        <th>실행일자</th>
                        <td title="workdate">{proc.workdate}</td>
                        <th>실행상태</th>
                        <td title="procstatus">{proc.procstatusnm}({proc.procstatus})</td>
                    </tr>
                    <tr>
                        <th class="borderTopSolid1pxGray">에러여부</th>
                        <td title="errflag">{proc.errflag}</td>
                        <th>작업결과건수</th>
                        <td title="resultcnt">{(proc.resultcnt??"").money()}</td>
                    </tr>
                    <tr>
                        <th>작업메시지</th>
                        <td colspan="3" title="procmsg">{proc.procmsg}</td>
                    </tr>
                    <tr>
                        <th>메모</th>
                        <td colspan="3" title="procmemo">{proc.procmemo}</td>
                    </tr>
                </tbody>
            </table>
            {#if from!="BatchResult"}
                <input type="text" class="comment" placeholder="작업 코멘트"
                    bind:this={NV.dom.procPopupComment}
                    bind:value={proc.comment}
                >
            {/if}
            
        </div>
    </div>
    <div slot="footer">
        {#if from!="BatchResult"}                        
        <button on:click={NV.action.btnProcC} disabled={!(["C","N"].indexOf(proc.procstatus)<0)}>스킵처리</button>
        <button on:click={NV.action.btnProcN} disabled={!(proc.procstatus=="E")}>자료없음</button>
        <button on:click={NV.action.btnProcQ} disabled={!(proc.procstatus=="C" && proc.reworkflag=="O")}>재 실 행</button>
        {/if}
        <button on:click={()=>{dispatch("close")}}>창 닫 기</button>
    </div>
</Popup>

<style lang="scss">    
    //PROC 상세 팝업 BODY의 테이블 영역
    div.tableInPopupWrapper{
        display:flex;
        flex-direction:column;
        justify-content:center;
        align-items:center;
        padding:0.8rem;
        table.tableInPopup{
            table-layout:fixed;
            border-collapse:collapse;
            border:solid 1px var(--tableBorderOuterColor);  
            border-color:var(--tableBorderColor);  
            width:100%;
            font-size:0.9rem;
            th {
                font-weight:bold;
                background-color:var(--bg1);
            }
            td {
                padding-left:0.5rem;
                line-height:1.8rem;
                word-wrap:break-word;
                &[title=jobcode],&[title=proccodeorg]{
                    color:var(--link);
                    font-weight:bold;
                }
            }
        }
        .comment{       
            margin:0.5rem 1rem 0rem 1rem;     
            width:calc(100% - 2rem);
            border:dashed 1px rgb(200 200 0);
            background-color:rgb(255 255 0/20%);
            padding:1rem;
            text-align:center;
        }
    }

</style>