<script>
const ID = "PopupJobDetail";

import { onMount, createEventDispatcher } from 'svelte';
import app from '$lib/app';
import Popup from '../Popup.svelte';

const dispatch = createEventDispatcher();

export let job = {};
</script>

<!-- JOB 상세 팝업 -->
<Popup on:close={()=>{dispatch("close")}}
    title={`JOB 상세정보(${job.jobcode})`}
    width={700}
    height={800}
    showFooter={true}
>
    <div slot="body">
        <div class="tableInPopupWrapper">
            <table class="tableInPopup" border="1">
                <colgroup>
                    <col width="100">
                    <col width="160">
                    <col width="100">
                    <col width="160">
                </colgroup>
                <tbody>
                    <tr>
                        <th>배치업무명</th>
                        <td colspan="3" title="jobname">{job.jobname}</td>
                    </tr>
                    <tr>
                        <th>실행주기</th>
                        <td title="typekindnm">{job.typekindnm}</td>
                        <th>대상서버</th>
                        <td title="jobsvrnm(jobsvr)">{job.jobsvrnm}({job.jobsvr})</td>
                    </tr>
                    <tr>
                        <th>배치그룹</th>
                        <td title="jobgroupnm(jobgroup)">{job.jobgroupnm}({job.jobgroup})</td>
                        <th>기준일자 FLAG</th>
                        <td title="workflagnm(workflag)">{job.workflagnm}({job.workflag})</td>
                    </tr>
                    <tr>
                        <th>실행파일경로</th>
                        <td colspan="3" title="jobfilepath">{job.jobfilepath}</td>
                    </tr>
                    <tr>
                        <th>배치파일실행명</th>
                        <td colspan="3" title="jobfile">{job.jobfile}</td>
                    </tr>
                    <tr>
                        <th>배치업무코드</th>
                        <td title="jobcode">{job.jobcode}</td>
                        <th>선행배치코드</th>
                        <td title="prejob">{job.prejob}</td>
                    </tr>
                    <tr>
                        <th>최소시작시간</th>
                        <td title="cuttime">{job.cuttime}</td>
                        <th>최대마감시간</th>
                        <td title="lasttime">{job.lasttime}</td>
                    </tr>
                    <tr>
                        <th>전체작업순서</th>
                        <td title="joborder">{job.joborder}</td>
                        <th>SKIP 가능여부</th>
                        <td title="skipflag">{job.skipflag}</td>
                    </tr>
                    <tr>
                        <th>작업등록자ID</th>
                        <td title="userid">{job.userid}</td>
                        <th>자료등록일시</th>
                        <td title="created">{job.created}</td>
                    </tr>
                    <tr>
                        <th>JOB 메모</th>
                        <td colspan="3" title="jobmemo">{job.jobmemo}</td>
                    </tr>
                </tbody>
            </table>
            <div style="height:0.5rem"/>
            <table class="tableInPopup" border="1">
                <colgroup>
                    <col width="100">
                    <col width="160">
                    <col width="100">
                    <col width="160">
                </colgroup>
                <tbody>
                    <tr>
                        <th>실행일자</th>
                        <td title="workdate">{job.workdate}</td>
                        <th>작업기준일자</th>
                        <td title="inputdate">{job.inputdate}</td>
                    </tr>
                    <tr>
                        <th>실행상태</th>
                        <td title="jobstatusnm(jobstatus)">{job.jobstatusnm}({job.jobstatus})</td>
                        <th>최종확인</th>
                        <td tiele="jobchk">{job.jobchk}</td>
                    </tr>
                    <tr>
                        <th>작업시작시각</th>
                        <td title="jobstarttime">{job.jobstarttime}</td>
                        <th>작업완료시각</th>
                        <td title="jobendtime">{job.jobendtime}</td>
                    </tr>
                    <tr>
                        <th>작업확인시각</th>
                        <td title="chktime">{job.chktime}</td>
                        <th>작업확인자ID</th>
                        <td title="chkuser">{job.chkuser}</td>
                    </tr>
                    <tr>
                        <th>지시사항</th>
                        <td colspan="3" title="ordermemo">{job.ordermemo}</td>
                    </tr>
                    <tr>
                        <th>작업자 코멘트</th>
                        <td colspan="3" title="workmemo">{job.workmemo??"-"}</td>
                    </tr>
                </tbody>
            </table>        
        </div>
    </div>
    <div slot="footer">
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
        padding:1rem;
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
                &[title=jobcode]{
                    color:var(--link);
                    font-weight:bold;
                }
                &[title=workmemo]{
                    height:3rem;
                }
            }            
        }
    }

</style>