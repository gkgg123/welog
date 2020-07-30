<template>
  <div class="mt-5">
    <h1>{{$route.params.id}}</h1>
    <h2>{{$route.params.pid}}</h2>
    <h3>{{title}}</h3>
    <aside class="aside-left">왼쪽입니다.</aside>
    <div class="aside-right">오른쪽입니다.</div>
    <v-md-editor
        class="border border-primary col-8 mx-auto"
        style="margin:5vh 0px; height:300vh;"
        id="detail"
        mode="preview"
        v-model="content"
      />
      
      <div id="context-menu" class="context-menu">
        <div class="item" data-toggle="modal" data-target="#exampleModal">
          수정요청
        </div>
        <div class="item">나가기</div>
      </div>

    <textarea class="border border-primary" placeholder="댓글을 남겨주세요" name="" id="" cols="30" rows="10">

    </textarea>



          <div
        class="modal fade"
        id="exampleModal"
        tabindex="-1"
        role="dialog"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
              <button
                type="button"
                class="close"
                data-dismiss="modal"
                aria-label="Close"
              >
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              {{ confirmText }}
            </div>
            <div class="modal-footer">
              <button
                type="button"
                class="btn btn-secondary"
                data-dismiss="modal"
              >
                Close
              </button>
              <button type="button" class="btn btn-primary">
                Save changes
              </button>
            </div>
          </div>
        </div>
      </div>



  </div>
</template>

<script>
export default {
    name : 'userPersonalPost',
    data() {
        return {
            title : 'text 중입니다.',
            content : '### 안의 내용입니다.',
            tagList : [],
            confirmText : '',

        }
    },
    methods : {
      headerChange(){
        const urlname = this.$route.params.id
        this.$store.commit('SET_header',urlname)
        this.$store.commit('SET_headerPath','@'+urlname)
      },
      //  오른쪽 버튼 Custom
      contextmenu(event) {
        const creatediv = document.querySelector(".v-md-editor__preview-wrapper");
        this.confirmText = document.getSelection().toString();
        creatediv.addEventListener("contextmenu", (event) => {
          event.preventDefault();
          var contextElement = document.getElementById("context-menu");
          contextElement.style.top = event.offsetY+ 270 + "px";
          contextElement.style.left = event.offsetX+ 270 + "px";
          this.confirmText = document.getSelection().toString();
          contextElement.classList.add("active");
        });
        creatediv.addEventListener("click", function() {
          var contextElement = document.getElementById("context-menu");
          contextElement.classList.remove("active");
        });
      },
      
    },
    created(){
      this.headerChange()
    },
    mounted(){
      this.contextmenu();
    }

}
</script>

<style>
.aside-left {
    float: left;
    position: fixed;
}
.aside-right {
    left: 85%;
    float: right;
    position: fixed;
}

#context-menu {
  position: absolute;
  z-index: 10000;
  width: 150px;
  background: #1b1a1a;
  border-radius: 5px;
  transform: scale(0);
  transform-origin: top left;
}
#context-menu.active {
  transform: scale(1);
  transition: transform 300ms ease-in-out;
}
#context-menu .item {
  padding: 8px 10px;
  font-size: 15px;
  color: #eee;
}
#context-menu .item:hover {
  background: #555;
}
#context-menu .item i {
  display: inline-block;
  margin-right: 5px;
}
#context-menu hr {
  margin: 2px 0px;
  border-color: #555;
}
</style>