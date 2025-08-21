<script>
    import { userStore } from '$lib/stores.js';

    

    import Base from '/src/layout/Base.svelte';
    import Content from '/src/layout/Content.svelte';
    import Frame from '/src/layout/Frame.svelte';
    import AuthCheck from '/src/comp/AuthCheck.svelte';
    // import Menu from '$lib/pieces/Menu.svelte';

    //console.log("/ $userStore", $userStore);

</script>


<AuthCheck>
    <Base>
        <Content>
            <Frame/>
        </Content>
    </Base>
</AuthCheck>