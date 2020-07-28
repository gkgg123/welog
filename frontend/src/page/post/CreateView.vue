<template>
  <div id="create" class="container">
    <div class="row">
      <div class="col-12 p-0" style="text-align:left; margin:5vh 0px;">
        <p id="title">새 글 쓰기</p>
      </div>
      <div id="tag" class="flex-column"></div>
      <input
        placeholder="Tag를 입력하고 Enter를 누르세요"
        @keyup.enter="tagEvent"
        @keyup.delete="deleteTag"
        class="taginput"
        v-model="inputTag"
        type="text"
      />
      <v-md-editor
        style="margin:5vh 0px; box-sizing: border-box;"
        id="create"
        placeholder="새 글을 작성해 보세요"
        v-model="text"
        @copy-code-success="handleCopyCodeSuccess"
      />

      <div id="context-menu" class="context-menu">
        <div class="item" data-toggle="modal" data-target="#exampleModal">
          수정요청
        </div>
        <div class="item">나가기</div>
      </div>

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
  </div>
</template>

<script>
export default {
  name: "CreateView",
  data() {
    return {
      text: "",
      inputTag: null,
      taglist: [],
      confirmText: "test중입니다.",
    };
  },
  methods: {
    tagEvent() {
      if (this.inputTag.length <= 15) {
        const tagDiv = document.querySelector("#tag");
        const btn = document.createElement("button");
        btn.innerText = this.inputTag;
        btn.setAttribute(
          "style",
          "background-color:#ddd; border: none; cursor:auto; border-radius: 16px; padding: 7px; margin:7px 3px; color:#0CA678"
        );
        tagDiv.append(btn);
        this.taglist.push(this.inputTag);
        this.inputTag = null;
      } else {
        this.inputTag = null;
        alert("Tag는 15자 이하로 입력해주세요");
      }
    },
    deleteTag() {
      if (
        this.taglist.length >= 1 &&
        (this.inputTag === null || this.inputTag === "")
      ) {
        if (
          confirm(
            this.taglist[this.taglist.length - 1] +
              " Tag를" +
              "정말 삭제하시겠습니까?"
          )
        ) {
          const tagDiv = document.querySelector("#tag");
          tagDiv.lastChild.remove();
          this.taglist.pop();
        }
      }
    },
    //  오른쪽 버튼 Custom
    contextmenu(event) {
      const creatediv = document.querySelector("#create");
      this.confirmText = document.getSelection().toString();
      creatediv.addEventListener("contextmenu", (event) => {
        event.preventDefault();
        var contextElement = document.getElementById("context-menu");
        contextElement.style.top = event.offsetY + "px";
        contextElement.style.left = event.offsetX + "px";
        this.confirmText = document.getSelection().toString();

        contextElement.classList.add("active");
      });
      creatediv.addEventListener("click", function() {
        var contextElement = document.getElementById("context-menu");
        contextElement.classList.remove("active");
      });
    },
    handleCopyCodeSuccess(code) {
      alert("성공적으로 복사되었습니다.");
      console.log(code);
    },
    changeHeight() {
      const mainMarkdownEditor = document.querySelector(".v-md-editor__main");
      mainMarkdownEditor.style.minHeight = "55vh";
    },
  },
  mounted() {
    this.contextmenu();
    this.changeHeight();
  },
};
</script>

<style scoped>
.taginput {
  font-size: 17px;
  padding-bottom: 4px;
  width: 17vw;
  border-bottom: 1px solid;
  margin: 1vh 5px;
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
#title {
  font-size: 40px;
  border-bottom: #ddd solid 4px;
  display: inline;
}
#tag-box {
  display: flex;
  align-content: center;
}
</style>
