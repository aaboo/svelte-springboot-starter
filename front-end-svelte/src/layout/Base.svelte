<script>
</script>

<!-- app.html로 이관
<svelte:head>
<script type="text/javascript" src="/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/js/utils.js"></script>
</svelte:head>
-->

<div id="base">
    <slot/>
</div>

<style lang="scss">
    div{
        width:100vw;
        height:100vh;
    }
</style>