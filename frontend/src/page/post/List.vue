<template>
    <div class="post">
        <div class="wrapB">
            <h2>전체글</h2>
            <div>

                <section class="post-list col-12">
                    <div class="w-100 col-xl-4 col-sm-6 col-12">
                        <div class="post-card">
                            <a>
                                <div :style="{backgroundImage:'url(https://www.ipcc.ch/site/assets/uploads/sites/3/2019/10/img-placeholder.png)'}" class="post-img"/>
                                
                                <div class="contents">
                                    <h3>
                                        제목
                                    </h3>
                                    <p class="content">컨텐츠</p>
                                    <span class="date">2020-06-19ㆍ</span>  
                                    <span class="comment">댓글 0개</span>
                                    
                                </div>
                            </a>

                            <div class="writer-wrap">
                                <a>
                                    닉네임
                                </a>
                                <span>
                                    ♥ 2
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="w-100 col-xl-4 col-sm-6 col-12">
                        <div class="post-card">
                            <a>
                                <div :style="{backgroundImage:'url(https://www.ipcc.ch/site/assets/uploads/sites/3/2019/10/img-placeholder.png)'}" class="post-img"/>
                                
                                <div class="contents">
                                    <h3>
                                        제목
                                    </h3>
                                    <p class="content">컨텐츠</p>
                                    <span class="date">2020-06-19ㆍ</span>  
                                    <span class="comment">댓글 0개</span>
                                    
                                </div>
                            </a>

                            <div class="writer-wrap">
                                <a>
                                    닉네임
                                </a>
                                <span>
                                    ♥ 2
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="w-100 col-xl-4 col-sm-6 col-12">
                        <div class="post-card">
                            <a>
                                <div :style="{backgroundImage:'url(https://www.ipcc.ch/site/assets/uploads/sites/3/2019/10/img-placeholder.png)'}" class="post-img"/>
                                
                                <div class="contents">
                                    <h3>
                                        제목
                                    </h3>
                                    <p class="content">컨텐츠</p>
                                    <span class="date">2020-06-19ㆍ</span>  
                                    <span class="comment">댓글 0개</span>
                                    
                                </div>
                            </a>

                            <div class="writer-wrap">
                                <a>
                                    닉네임
                                </a>
                                <span>
                                    ♥ 2
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="w-100 col-xl-4 col-sm-6 col-12">
                        <div class="post-card">
                            <a>
                                <div :style="{backgroundImage:'url(https://www.ipcc.ch/site/assets/uploads/sites/3/2019/10/img-placeholder.png)'}" class="post-img"/>
                                
                                <div class="contents">
                                    <h3>
                                        제목
                                    </h3>
                                    <p class="content">컨텐츠</p>
                                    <span class="date">2020-06-19ㆍ</span>  
                                    <span class="comment">댓글 0개</span>
                                    
                                </div>
                            </a>

                            <div class="writer-wrap">
                                <a>
                                    닉네임
                                </a>
                                <span>
                                    ♥ 2
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="w-100 col-xl-4 col-sm-6 col-12">
                        <div class="post-card">
                            <a>
                                <div :style="{backgroundImage:'url(https://www.ipcc.ch/site/assets/uploads/sites/3/2019/10/img-placeholder.png)'}" class="post-img"/>
                                
                                <div class="contents">
                                    <h3>
                                        제목
                                    </h3>
                                    <p class="content">컨텐츠</p>
                                    <span class="date">2020-06-19ㆍ</span>  
                                    <span class="comment">댓글 0개</span>
                                    
                                </div>
                            </a>

                            <div class="writer-wrap">
                                <a>
                                    닉네임
                                </a>
                                <span>
                                    ♥ 2
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="w-100 col-xl-4 col-sm-6 col-12">
                        <div class="post-card">
                            <a>
                                <div :style="{backgroundImage:'url(https://www.ipcc.ch/site/assets/uploads/sites/3/2019/10/img-placeholder.png)'}" class="post-img"/>
                                
                                <div class="contents">
                                    <h3>
                                        제목
                                    </h3>
                                    <p class="content">컨텐츠</p>
                                    <span class="date">2020-06-19ㆍ</span>  
                                    <span class="comment">댓글 0개</span>
                                    
                                </div>
                            </a>

                            <div class="writer-wrap">
                                <a>
                                    닉네임
                                </a>
                                <span>
                                    ♥ 2
                                </span>
                            </div>
                        </div>
                    </div>
                    
                </section>

                <div class="tag-list-wrap">
                    <h4>공지사항</h4>
                    <ul class="tag-list mb-5">
                        <li>공지사항1</li>
                        <li>공지사항2</li>
                        <li>공지사항3</li>
                    </ul>
                    <h4>인기태그</h4>
                    <ul class="tag-list">
                        <li>
                            #태그1 (8)
                        </li> 
                        <li>
                            #태그2 (5)
                        </li> 
                        <li>
                            #태그3 (2)
                        </li>
                        <a href="#" class="text-secondary"><u>더보기</u></a>
                    </ul>
                </div>
            </div>
        </div>

        

    </div>
</template>

<style>
ul{
    list-style: none;
}
</style>
 
<script>
    import '../../assets/css/post.scss'

    export default {
        name:"Post",
        components:{
        },
        watch: {
        },
        created() {
        },
        methods: {
        },
        data: () => {
            return {
            }
        }
    }
</script>