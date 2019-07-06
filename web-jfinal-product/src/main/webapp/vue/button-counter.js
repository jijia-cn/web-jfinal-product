/**
 * 
 */

// 定义一个名为 button-counter 的新组件
Vue.component('button-counter', {
  data: function () {
    return {
      count: 0
    }
  },
  methods:{
	  btnClick:function(){
		this.$data.count++;
	  }
  },
  template: '<button v-on:click="btnClick()">You clicked me {{ count }} times.</button>'
});