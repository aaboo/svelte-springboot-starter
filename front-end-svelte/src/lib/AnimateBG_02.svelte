<script>
	import { onMount } from 'svelte';
	import p5 from 'p5';


	export let sketch;


	onMount(()=>{
		sketch = new p5(sketchFunction);
	});


	/* 
	* https://p5js.org/ko/examples/simulate-particles.html
	*/
	//복수의 파티클들을 추가하기 위한 배열
	let particles = [];

	//이 클래스는 각 파티클의 속성들을 표현합니다.
	class Particle {
		//파티클의 좌표값, 반경, 그리고 속도를
		//두 좌표축에 의거하여 설정합니다.
		constructor(p){
			this.p = p;
			this.x = p.random(0,p.width);
			this.y = p.random(0,p.height);
			this.r = p.random(1,8);
			this.xSpeed = p.random(-2,2);
			this.ySpeed = p.random(-1,1.5);
		}
		
		//파티클 생성하기
		createParticle() {
			this.p.noStroke();
			this.p.fill('rgba(200,169,169,0.5)');
			this.p.circle(this.x,this.y,this.r);
		}
		
		//파티클이 움직이도록 설정하기
		moveParticle() {
			if(this.x < 0 || this.x > this.p.width)
				this.xSpeed*=-1;
			if(this.y < 0 || this.y > this.p.height)
				this.ySpeed*=-1;
			this.x+=this.xSpeed;
			this.y+=this.ySpeed;
		}
		
		//이 함수는 특정 거리 안쪽에 위치한 파티클들 사이에 연결선을 만듭니다.
		joinParticles(paraticles) {
			particles.forEach(element =>{
				let dis = this.p.dist(this.x,this.y,element.x,element.y);
				if(dis<85) {
					this.p.stroke('rgba(100,100,100,0.04)');
					this.p.line(this.x,this.y,element.x,element.y);
				}
			});
		}
	}

	function sketchFunction(p){
		p.setup = ()=>{
			let target = document.querySelector("canvas#loginBgCanvas");
			p.createCanvas(p.windowWidth, p.windowHeight, target);
			//target.style="width:100%;height:100%";
			for(let i = 0;i<p.width/10;i++){
				particles = [...particles, new Particle(p)];
			}
			particles = particles;
		}
			
		p.draw = ()=>{
			p.background('#eaeaea');
			for(let i = 0;i<particles.length;i++) {
				particles[i].createParticle();
				particles[i].moveParticle();
				particles[i].joinParticles(particles.slice(i));
			}
		}
	}

	function reRendering(){
		particles = [];
		sketch.setup();
		sketch.draw();
	}

	let resizeTimer = null;
	function resize(e){
		clearTimeout(resizeTimer);
		resizeTimer = setTimeout(()=>{
			reRendering();
		}, 100);
	}

</script>

<svelte:window on:resize={resize}/>

<canvas id="loginBgCanvas"/>

<style>
:global(body){
	overflow:hidden;
}
</style>