<script>
    import { onMount } from 'svelte';

    import { userStore } from '$lib/stores';
    import app from '$lib/app';
    import icon from '$lib/icon';

    onMount(async ()=>{
        try{
            const res = await app.fetch("/auth/userInfo");
            app.modal.close();
            const authCheckInfo = res.data;
            let ss = JSON.parse(sessionStorage.getItem(app.name))//JSON.parse(??"{}");        

            userStore.update(authCheckInfo);
            //console.log($userStore, ss);
            
            if($userStore==null || !ss){
              location.href="/login";
            }
        }catch(e){
            //alert("로그인이 필요합니다(4)");
            //location.href="/login";
        }
    });
</script>

{#if $userStore==null}
    <div style="color:gray">
        {@html icon.loading2(1.8)}
        Loading
    </div>
{:else}
    <slot/>
{/if}

<style lang="scss">
    div {
        display:flex;
        flex-direction:column;
        align-items:center;
        justify-content:center;
        width:100vw;
        height:100vh;
        color:white;
        background-color:rgb(0 0 0/20%);
        font-size:1rem;
    }
</style>