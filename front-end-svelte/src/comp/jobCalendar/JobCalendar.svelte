<script>
const ID = "jobCalendar";

import { onMount } from 'svelte';
import app from '$lib/app';
import Popup from '../Popup.svelte';
import tuiGrid from '$lib/tuiGrid';
import 'tui-grid/dist/tui-grid.css';
import icon from '$lib/icon';

onMount(() => {        
    NV.init();
    //window.NV = NV;
});

const NV = {
    init: function(){
        let now = new Date();
        let year = now.getFullYear();
        let month = now.getMonth();
        this.calendar.data = {
            now: now
            , year: year
            , month: month
            , daysInMonth: new Date(year, month + 1, 0).getDate()
            , startingDay: new Date(year, month, 1).getDay()
            , days: []
        }
        this.calendar.render();
    }
};

NV.calendar = {
    data:{}
    //달력 그리기
    , render: function(){
        let dt = this.data;
        dt.days = [];
        let dayCounter = 1;

        for (let i = 0; i < 6; i++) {
            let row = [];

            for (let j = 0; j < 7; j++) {
                let cell = {};

                if (i === 0 && j < dt.startingDay) {
                    cell.day = new Date(dt.year, dt.month, 0).getDate() - (dt.startingDay - j) + 1;
                    cell.yyyymmdd = (new Date(dt.year, dt.month-1, cell.day)).print("yyyymmdd");
                    cell.prevMonth = true;
                } else if (dayCounter > dt.daysInMonth) {
                    cell.day = dayCounter - dt.daysInMonth;
                    cell.yyyymmdd = (new Date(dt.year, dt.month+1, cell.day)).print("yyyymmdd");
                    dayCounter++;
                    cell.nextMonth = true;
                } else {
                    cell.day = dayCounter;
                    cell.yyyymmdd = (new Date(dt.year, dt.month, cell.day)).print("yyyymmdd");
                    cell.currentMonth = true;
                    if (dayCounter === dt.now.getDate() && dt.year === dt.now.getFullYear() && dt.month === dt.now.getMonth()) {
                        cell.currentDay = true;
                    }
                    dayCounter++;
                }
                row = [...row, cell];
            }
            dt.days = [...dt.days, row];
        }
        //console.log(year, month, (new Date(year, month+1, 0)).print("yyyymm"));
        NV.action.search();
    }
    //1년전 이동
    , goPrevYear: ()=>{
        let dt = NV.calendar.data;
        dt.year = dt.year - 1;
        dt.daysInMonth = new Date(dt.year, dt.month + 1, 0).getDate();
        dt.startingDay = new Date(dt.year, dt.month, 1).getDay();
        NV.calendar.render();
    }
    //1년후 이동
    , goNextYear: ()=>{
        let dt = NV.calendar.data;
        dt.year = dt.year + 1;
        dt.daysInMonth = new Date(dt.year, dt.month + 1, 0).getDate();
        dt.startingDay = new Date(dt.year, dt.month, 1).getDay();
        NV.calendar.render();
    }
    //1달전 이동
    , goPrevMonth: ()=>{
        let dt = NV.calendar.data;
        dt.month = dt.month === 0 ? 11 : dt.month - 1;
        dt.year = dt.month === 11 ? dt.year - 1 : dt.year;
        dt.daysInMonth = new Date(dt.year, dt.month + 1, 0).getDate();
        dt.startingDay = new Date(dt.year, dt.month, 1).getDay();
        NV.calendar.render();
    }
    //1달후 이동
    , goNextMonth: ()=>{
        const dt = NV.calendar.data;
        dt.month = dt.month === 11 ? 0 : dt.month + 1;
        dt.year = dt.month === 0 ? dt.year + 1 : dt.year;
        dt.daysInMonth = new Date(dt.year, dt.month + 1, 0).getDate();
        dt.startingDay = new Date(dt.year, dt.month, 1).getDay();
        NV.calendar.render();
    }
    //오늘 이동
    , goToday: ()=>{
        const dt = NV.calendar.data;
        dt.month = dt.now.getMonth();
        dt.year = dt.now.getFullYear();
        dt.daysInMonth = new Date(dt.year, dt.month + 1, 0).getDate();
        dt.startingDay = new Date(dt.year, dt.month, 1).getDay();
        NV.calendar.render();
    }
    //새로고침
    , refresh: ()=>{
        NV.calendar.render();
    }
}

NV.action = {
    //조회        
    search: function(){
        const dt = NV.calendar.data;
        //console.log(ID, param, JSON.stringify(param), JSON.stringify(param)=="{}");
        const param = {
            date1: dt.days[0][0].yyyymmdd
            , date2: dt.days[dt.days.length-1][6].yyyymmdd
        }
        //console.log(param);
        app.ss.setSearch(param);
        //console.log(param);
        app.modal.open();//모달
        app.fetch("/jobCalendar/selectJobCalendar", {...param})
        .then(async (response)=>{
            if(response.code=="OK"){
                let today = (new Date()).print("yyyy-mm-dd");
                //console.log(response);
                NV.calendar.data.yyyymmdds = {};
                response.data.map((item, idx)=>{
                    NV.calendar.data.yyyymmdds[item.workdate] = {
                        total: item.total
                        , hol: item.hol
                    }
                });
            }else{
                alert(response.message);
            }
            app.modal.close();
        })
        .catch((e)=>{
            //console.log(e);
            app.modal.close();
        }); 
    }
    //상세조회
    , searchDetail: function(workdate){
        const pd = NV.popup.data;
        NV.calendar.data.sltDay=workdate;
        const param = {
            workdate: workdate.replace(/[^\d]/g,'')
        }
        //console.log(param);
        app.modal.open();//모달
        app.fetch("/jobCalendar/selectJobCalendarDetail", {...param})
        .then(async (response)=>{
            //console.log(response);
            pd.param = {...param};
            pd.title = `상세정보(${param.workdate})`;
            pd.showFooter = response.data.length<=0;
            pd.bodyHeight = pd.height - 50 - (pd.showFooter?70:0) - 0;
            //-- 이 단계를 거쳐야 grid가 refresh 됨
            await new Promise((fulfill)=>{
                NV.popup.display = true;
                fulfill();                
            });
            NV.grid.init([...response.data]);
            app.modal.close();
        })
        .catch((a,b,c)=>{
            //console.log(a,b,c);
            app.modal.close();
        }); 
    }
    //배치등록
    , insertJobCtrl: async function(ev){
        const param = {...NV.popup.data.param}
        //console.log(param);
        app.modal.open();//모달
        const response = await app.fetch("/jobCalendar/insertJobCalendar", {...param});
        //console.log(response);
        NV.popup.display = false;        
        NV.action.search();
        NV.action.searchDetail(param.workdate);
    }
}

NV.grid= {
    target: null
    , showMeTheFooter: false
    , init: async function(data){
        //console.log("NV.popup.data.bodyHeight", NV.popup.data.bodyHeight - 81);
        const option = {
            el: NV.grid.target
            , scrollX: true
            , scrollY: true
            , width: NV.popup.data.width
            , bodyHeight: NV.popup.data.bodyHeight - 80 - 80 - 1//gridHead - gridSummary - border//'fitToParent'
            , columns: [
                {header:'No', name:'no', align:"center", width:50, formatter:(item)=>{  
                    return item.row.rowKey+1;
                }}
                , {header:'서버', name:'jobsvr', align:"center", width:120}
                , {header:'코드명', name:'jobcode', align:"center", width:100}
                , {header:'업무명', name:'jobname', align:"left"}
                , {header:'실행배치', name:'jobfile', align:"left", width:250}
                , {header:'등록자ID', name:'userid', align:"center", width:100}
            ]
            , summary:{
                position:"bottom"
                , height:40
                , columnContent:{
                    jobname: {
                        template: (sum)=>{
                            return `<div style="text-align:left">total: ${sum.cnt}</div>`;
                        }
                    }
                }
            }
        }
        this.target = tuiGrid.make(option);
        this.target.resetData([...data]);
    }
}

//팝업
NV.popup = {
    display: false
    , data: {
        title:""
        , width:1100
        , height:800
    }
}
</script>



<jobCalendar>
    <div id="head">
        <button on:click={NV.calendar.goToday} class="btnToday">오늘</button>
        <button on:click={NV.calendar.goPrevYear} class="btnYear">{@html icon.left2(1.5)}</button> 
        <button on:click={NV.calendar.goPrevMonth}>{@html icon.left1(1.5)}</button>
        <span style="text-align:center;">&nbsp;
            {NV.calendar.data.year??""}년
            {(NV.calendar.data?.month??"0") +1}월
        &nbsp;</span>
        <button on:click={NV.calendar.goNextMonth}>{@html icon.right1(1.5)}</button>
        <button on:click={NV.calendar.goNextYear} class="btnYear">{@html icon.right2(1.5)}</button>
        <button on:click={NV.calendar.refresh} class="btnToday">새로고침</button>
    </div>
    <table cellspacing="1">
        <thead>
        <tr>
            <th class="sunday">일</th>
            <th>월</th>
            <th>화</th>
            <th>수</th>
            <th>목</th>
            <th>금</th>
            <th class="saturday">토</th>
        </tr>
        </thead>
        <tbody>
            {#each (NV.calendar.data?.days??[]) as row}
            <tr>
                {#each row as cell, cellIdx}
                <td
                    class:prev-month={cell.prevMonth}
                    class:next-month={cell.nextMonth}
                    class:current-day={cell.currentDay}
                    class:current-month={cell.currentMonth}
                    class:on={NV.calendar.data.sltDay==cell.yyyymmdd}
                >
                    <div class="day"
                        class:hol={NV.calendar.data.yyyymmdds[cell.yyyymmdd]?.hol=="휴일"}
                        class:sunday={cellIdx==0}
                        class:saturday={cellIdx==6}
                        class:current-month={cell.currentMonth}
                    >
                        {cell.day}
                    </div>
                    <div class="job" 
                        on:click={NV.action.searchDetail(cell.yyyymmdd)}
                        on:keyup
                        role="button" tabindex={cellIdx}
                    >
                        <!-- {cell.yyyymmdd} -->
                        {NV.calendar?.data.yyyymmdds[cell.yyyymmdd]?.total??""}
                    </div>
                </td>
                {/each}
            </tr>
            {/each}
        </tbody>
    </table>
    
</jobCalendar>

<!-- 팝업 -->
{#if NV.popup.display==true}
    <Popup on:close={()=>{NV.popup.display=false}}
        showFooter={NV.popup.data.showFooter}
        title={NV.popup.data.title}
        width={1100}
        height={800}
    >
        <div slot="body" bind:this={NV.grid.target}/>
        <div slot="footer">
            <button on:click={NV.action.insertJobCtrl}>등록</button>
        </div>
    </Popup>
{/if}



<style lang="scss">
jobCalendar{    
    display:block;
    position:relative;
    left:-2px;
    //border:solid 1px white;
    width:calc(100% + 2px);
    min-width:650px;
    //height:100vh;
    margin:1px;
    div#head{
        display:flex;
        align-items:center;
        justify-content:center;
        background-color:#eee;
        font-size:1.5rem;
        //font-weight:600;
        height:60px;
        button{
            padding:0 0.2rem;
            height:2rem;
            background-color:transparent!important; 
            color:#999;
            font-size:1rem;
            //border:solid 1px rgba(0 0 0/10%);
            &:hover{
                background-color:white!important;
            }
            &.btnYear{
                letter-spacing:-5px;
            }
            &.btnToday{
                color:#333;
                padding:0 0.5rem;
            }
        }
    }
    table {
        //border-collapse: collapse;
        position:relative;
        width:100%;
        height:calc(100vh - 50px - 60px);
        font-size:1rem;
        background-color:lightgray;
    }

    th, td {
        //border: solid 1px lightgray;
        border:0;
        padding: 8px;
        text-align: center;
        width:calc(100% / 7);
    }

    th {
        background-color: white;
        font-size:1rem;
        font-weight:500;
        &.sunday { color:tomato;font-weight:500; }
        &.saturday { color:royalblue;font-weight:500; }
    }

    td {
        position:relative;
        cursor:pointer;
        div.day{
            position:absolute;
            top:4px;
            left:8px;
            font-size:1.4rem;
            background-color:transparent;
            &.hol.current-month { color:tomato;font-weight:500; }
            &.sunday.current-month { color:tomato;font-weight:500; }
            &.saturday.current-month { color:royalblue;font-weight:500; }
        }
        div.job {
            font-size:1rem;
            &:hover{color:var(--link);}
        }
        &:hover{ background-color:#eee;}
        &.on{ border:solid 2px rgb(0 0 0/10%); }
    }

    .current-month {
        background-color:white;
    }
    .prev-month {
        color: #888;
        background-color:#ddd;
        &:hover{ background-color:#d0d0d0;}
    }

    .next-month {
        color: #888;
        background-color:#ddd;
        &:hover{ background-color:#d0d0d0;}
    }

    .current-day {
        background-color: #dbe7f3;
        &:hover{ background-color:#cfdbe7}
        &:active{ background-color:#b0bdca}
    }

}
</style>