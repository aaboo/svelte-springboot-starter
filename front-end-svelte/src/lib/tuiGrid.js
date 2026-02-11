import Grid from 'tui-grid';

const TuiGrid = function(){
    //Grid 일반설정
    Grid.applyTheme('default', {
    //Grid.applyTheme('striped', {
        row:{
            hover: {
                background: "#efefef"//"rgba(255 0 0/0.5%)"
            }
        }
        , cell: {
            normal: {
                background: '#ffffff',             
                border:"rgba(0,0,0,0.15)"
            }
        }
    });
    Grid.setLanguage('ko', {
        display:{
            noData: '자료가 없습니다.'
            , loadingData: 'Loading...'
        }
    });
}

TuiGrid.prototype = {
    make: function(gridOptions){
        //gridOptions["contextMenu"] = (a, b, c)=>{ console.log(a,b,c);};
        //gridOptions["contextMenu"] = null; //기능을 해제하면, Grid 마우스 우클릭 메뉴(엑셀변환 등)가 활성화된다.(개발할 때만 주석해제하고 쓰면됨)
        gridOptions["columnOptions"] = {
            resizable: true
        }
        gridOptions["draggable"] = false; //row, column을 이동시킬 수 있다.
        const grid = new Grid(gridOptions);
        return grid;
    }
}

const tuiGrid = new TuiGrid();


export default tuiGrid;