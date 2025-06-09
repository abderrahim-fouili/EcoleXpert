import{_ as i,at as l,Q as d,S as u,a6 as c,a8 as p,a5 as h,a3 as f,ad as g,aC as m,a as v}from"./index-Bl2lQPd7.js";var e=null;const w={data(){return{data:null}},computed:{...l({user:"user/user",config:"user/config",current:"user/current"}),eleve(){if(this.current&&this.current.hasOwnProperty("eleve"))return this.current.eleve},annee(){if(this.current&&this.current.hasOwnProperty("annee"))return this.current.annee}},methods:{getData(){return v("post","/iplus",{code:this.eleve.code}).then(t=>t.data).catch(t=>{this.$q.notify("Cette fonctionnalité  n'est pas actuellement configurée pour cet élève")})},show(){e?e.show():this.open()},open(){e&&(e.close(),e=null);let t="_blank",s="location=no,hidden=yes,footer=yes,clearcache=yes,clearsessioncache=yes",n="https://www.iplusinteractif.com/primaire/login";this.getData().then(r=>{if(this.data=r,this.data&&this.data.id){let a={code:this.data.code_groupe,login:this.data.login,password:this.data.passe};e=cordova.InAppBrowser.open(n,t,s),e.addEventListener("loadstart",()=>{}),e.addEventListener("loadstop",()=>{e.executeScript({code:`
              var accesscode = document.getElementsByName('groupId');
              var login =  document.getElementsByName('login');
              var password =  document.getElementsByName('password');
              var form = document.getElementsByTagName('form')
              if(accesscode && accesscode.length){
                //console.log(accesscode,login,password);
                accesscode[0].value = '${a.code}';
                login[0].value = '${a.login}';
                password[0].value = '${a.password}';
                //console.log(accesscode,login,password);
                document.querySelector('form button[type=submit]').click()
              }
              

              `}),e.show()}),e.addEventListener("loaderror",o=>{console.log("Error "+o)}),e.addEventListener("message",()=>{}),e.addEventListener("exit",()=>{e=null})}else this.$q.notify("Cette fonctionnalité  n'est pas actuellement configurée pour cet élève")})}},unmounted(){e&&(e.close(),e=null)},mounted(){this.setTitle("i+ interactif"),this.open()}},y={style:{padding:"26px 8px 14px"}};function _(t,s,n,r,a,o){return u(),d(m,{padding:"",class:"page-content"},{default:c(()=>[p("div",y,[h(f,{onClick:o.show,class:"full-width"},{default:c(()=>s[0]||(s[0]=[g("Lancer i+ interactif")])),_:1},8,["onClick"])])]),_:1})}const B=i(w,[["render",_]]);export{B as default};
