import './polyfills.server.mjs';
import{b as F,c as _,d as C,e as U,f as W,g as Z,h as K,k as X,o as Y,p as tt,r as D,s as et}from"./chunk-WI3ZIAVW.mjs";import{$ as v,Aa as m,Ba as G,Ca as r,Da as i,Ea as g,Fa as O,Ga as u,Ha as l,Ia as T,J as L,Ja as a,Ka as b,La as w,Ma as d,Na as I,O as V,S as s,_ as h,cb as J,da as q,qa as c,ra as P,s as z,za as f}from"./chunk-XMLTB2OA.mjs";import{h as B}from"./chunk-5XUXGTUW.mjs";var N=class n{title="bot-list";static \u0275fac=function(t){return new(t||n)};static \u0275cmp=s({type:n,selectors:[["app-root"]],standalone:!0,features:[d],decls:1,vars:0,template:function(t,o){t&1&&g(0,"router-outlet")},dependencies:[tt]})};var y=class n{constructor(e){this.http=e}APP_NAME="scriptagher";BASE_PATH=`https://raw.githubusercontent.com/diegofcj/${this.APP_NAME}`;BASE_SOURCE=`https://github.com/DiegoFCJ/${this.APP_NAME}`;BOTS="bots";GH_PAGES=`${this.BASE_PATH}/gh-pages/browser/${this.BOTS}/bots.json`;TREE_BOT_LIST=`${this.BASE_SOURCE}/tree/gh-pages/browser/${this.BOTS}`;botDetailsPath(e,t){return`${this.TREE_BOT_LIST}/${t}/${e}/Bot.json`}getBotsConfig(){let e=this.GH_PAGES;return this.http.get(e)}openBot(e){e.sourcePath=`${this.TREE_BOT_LIST}/${e.language}/${e.botName}`,window.open(e.sourcePath||"#","_blank")}getBotDetails(e){return console.log("botJsonPath",e),this.http.get(e)}downloadBot(e,t){let o=`${this.BASE_PATH}/${e}/${t}/${t}.zip`;return this.http.get(o,{responseType:"blob"})}getBotDetailsByName(e){let t=`${this.BASE_PATH}/bots/${e}/Bot.json`;return this.http.get(t)}static \u0275fac=function(t){return new(t||n)(V(U))};static \u0275prov=L({token:n,factory:n.\u0275fac,providedIn:"root"})};function ct(n,e){if(n&1){let t=O();r(0,"button",3),u("click",function(){h(t);let p=l();return v(p.navigateToPdfToQcm())}),a(1," Vai al Converter PDF to QCM "),i()}}var k=class n{constructor(e,t){this.router=e;this.botService=t}bot;language="";download=new q;openBot(){this.botService.openBot(this.bot)}downloadBot(){this.download.emit()}navigateToPdfToQcm(){try{this.router.navigate(["/pdf-to-qcm"])}catch(e){console.error("Errore durante la navigazione verso la pagina PDF to QCM:",e)}}static \u0275fac=function(t){return new(t||n)(P(D),P(y))};static \u0275cmp=s({type:n,selectors:[["app-bot-card"]],inputs:{bot:"bot",language:"language"},outputs:{download:"download"},standalone:!0,features:[d],decls:14,vars:4,consts:[[1,"bot"],["class","nav-link-button",3,"click",4,"ngIf"],[3,"click"],[1,"nav-link-button",3,"click"]],template:function(t,o){t&1&&(r(0,"div",0)(1,"h3"),a(2),i(),r(3,"p"),a(4),i(),r(5,"p"),a(6,"Start Command "),g(7,"br"),a(8),i(),f(9,ct,2,0,"button",1),r(10,"button",2),u("click",function(){return o.openBot()}),a(11,"View Source"),i(),r(12,"button",2),u("click",function(){return o.downloadBot()}),a(13,"Download"),i()()),t&2&&(c(2),b(o.bot.botName),c(2),w("Description: ",o.bot.description||"No description available",""),c(4),b(o.bot.startCommand||"No start commands provided"),c(),m("ngIf",o.bot.botName==="Zipper"))},dependencies:[C,_],styles:[".bot[_ngcontent-%COMP%]{background-color:var(--bg-card, #333);color:var(--text-card, #eee);padding:1rem;border-radius:8px;margin:1rem 0}.bot[_ngcontent-%COMP%]   .nav-link-button[_ngcontent-%COMP%]{background-color:var(--button-bg, #5cb85c);cursor:pointer}.bot[_ngcontent-%COMP%]   .nav-link-button[_ngcontent-%COMP%]:hover{background-color:var(--button-hover, #4cae4c)}"]})};function pt(n,e){if(n&1){let t=O();r(0,"app-bot-card",2),u("download",function(){let p=h(t).$implicit,M=l();return v(M.downloadBot(p))}),i()}if(n&2){let t=e.$implicit,o=l();m("bot",t)("language",o.language)}}var H=class n{language="";bots=[];downloadBot(e){console.log(`Downloading ${e.botName}...`)}capitalize(e){return e.charAt(0).toUpperCase()+e.slice(1)}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=s({type:n,selectors:[["app-bot-section"]],inputs:{language:"language",bots:"bots"},standalone:!0,features:[d],decls:5,vars:2,consts:[[1,"bot-container"],[3,"bot","language","download",4,"ngFor","ngForOf"],[3,"download","bot","language"]],template:function(t,o){t&1&&(r(0,"section")(1,"h2"),a(2),i(),r(3,"div",0),f(4,pt,1,2,"app-bot-card",1),i()()),t&2&&(c(2),w("",o.capitalize(o.language)," Bots"),c(2),m("ngForOf",o.bots))},dependencies:[C,F,k],styles:["section[_ngcontent-%COMP%]{display:flex;flex-direction:column;align-items:center;width:100%}h2[_ngcontent-%COMP%]{color:#e0e0e0}.bot-container[_ngcontent-%COMP%]{display:flex;flex-wrap:wrap;flex-direction:row;gap:1rem;justify-content:center}"]})};function st(n,e){if(n&1){let t=O();r(0,"div",3)(1,"span",4),a(2,"Scriptagher"),i(),r(3,"span",5),a(4,"PDF to QCM"),i(),r(5,"div",6),u("click",function(){h(t);let p=l();return v(p.navigateHome())}),r(6,"div",6),u("click",function(){h(t);let p=l();return v(p.navigateHome())}),g(7,"img",7),i()()()}}function mt(n,e){n&1&&(r(0,"div",8)(1,"h1"),a(2,"Welcome to the Bot List"),i(),r(3,"p"),a(4,"Here is a list of available bots, organized by language."),i()())}var E=class n{constructor(e){this.router=e;this.router.events.pipe(z(t=>t instanceof Y)).subscribe(t=>{this.isPdfToQcmPage=t.url==="/pdf-to-qcm"})}isPdfToQcmPage=!1;navigateHome(){this.router.navigate(["/"])}static \u0275fac=function(t){return new(t||n)(P(D))};static \u0275cmp=s({type:n,selectors:[["app-header"]],standalone:!0,features:[d],decls:5,vars:4,consts:[["normalHeader",""],[1,"header-content"],["class","pdf-header",4,"ngIf","ngIfElse"],[1,"pdf-header"],[1,"app-name"],[1,"current-page"],[1,"home-button",3,"click"],["src","/icons/icons8-home-64.png"],[1,"normal-header"]],template:function(t,o){if(t&1&&(r(0,"header")(1,"div",1),f(2,st,8,0,"div",2)(3,mt,5,0,"ng-template",null,0,I),i()()),t&2){let p=T(4);G("pdf-page",o.isPdfToQcmPage),c(2),m("ngIf",o.isPdfToQcmPage)("ngIfElse",p)}},dependencies:[C,_],styles:["header[_ngcontent-%COMP%]{text-align:center;padding:1rem;background:var(--header-bg, #222);color:#fff;transition:background-color .3s ease}header.pdf-page[_ngcontent-%COMP%]{height:5cap;background:var(--pdf-header-bg, #333);position:relative;display:flex;align-items:center;justify-content:space-between;padding:0 1rem}header[_ngcontent-%COMP%]   .header-content[_ngcontent-%COMP%]{position:relative;width:100%;display:flex;justify-content:space-between;gap:.5rem}header[_ngcontent-%COMP%]   .header-content[_ngcontent-%COMP%]   .pdf-header[_ngcontent-%COMP%]{position:absolute;left:0;right:0;top:-20px;display:flex;align-items:center;justify-content:space-between;gap:1rem}header[_ngcontent-%COMP%]   .header-content[_ngcontent-%COMP%]   .pdf-header[_ngcontent-%COMP%]   .app-name[_ngcontent-%COMP%]{font-size:2rem;color:#fff}header[_ngcontent-%COMP%]   .header-content[_ngcontent-%COMP%]   .pdf-header[_ngcontent-%COMP%]   .current-page[_ngcontent-%COMP%]{color:#ddd;font-size:2rem}header[_ngcontent-%COMP%]   .header-content[_ngcontent-%COMP%]   .pdf-header[_ngcontent-%COMP%]   .home-button[_ngcontent-%COMP%]   img[_ngcontent-%COMP%]{cursor:pointer;width:35px;height:35px;transition:transform .2s ease}header[_ngcontent-%COMP%]   .header-content[_ngcontent-%COMP%]   .pdf-header[_ngcontent-%COMP%]   .home-button[_ngcontent-%COMP%]   img[_ngcontent-%COMP%]:hover{transform:scale(1.1);filter:invert(60%)}header[_ngcontent-%COMP%]   .header-content[_ngcontent-%COMP%]   .normal-header[_ngcontent-%COMP%]   h1[_ngcontent-%COMP%]{font-size:2rem}header[_ngcontent-%COMP%]   .header-content[_ngcontent-%COMP%]   .normal-header[_ngcontent-%COMP%]   p[_ngcontent-%COMP%]{font-size:1rem;opacity:.7}"]})};var $=class n{static \u0275fac=function(t){return new(t||n)};static \u0275cmp=s({type:n,selectors:[["app-footer"]],standalone:!0,features:[d],decls:3,vars:0,template:function(t,o){t&1&&(r(0,"footer")(1,"p"),a(2,"\xA9 2024 Scriptagher"),i()())},styles:["footer[_ngcontent-%COMP%]{text-align:center;padding:1rem;background-color:var(--bg-footer, #111);color:var(--text-footer, #ccc)}"]})};function lt(n,e){if(n&1&&g(0,"app-bot-section",3),n&2){let t=e.$implicit;m("language",t.language)("bots",t.botDetails)}}function dt(n,e){if(n&1&&(r(0,"main"),f(1,lt,1,2,"app-bot-section",2),i()),n&2){let t=l();c(),m("ngForOf",t.botSections)}}function ft(n,e){if(n&1&&(r(0,"p"),a(1),i()),n&2){let t=l();c(),b(t.errorMessage||"No bots available.")}}var Q=class n{constructor(e){this.botService=e}botSections=[];errorMessage="";ngOnInit(){this.populateBotList()}populateBotList(){this.botService.getBotsConfig().subscribe({next:e=>B(this,null,function*(){this.botSections=[];for(let t in e){let o=e[t];if(!o||o.length===0)continue;let p=yield Promise.all(o.map(M=>B(this,null,function*(){let j=this.botService.botDetailsPath(M.botName,t);console.log("botJsonPath v4t5t54vb45tbv",j);try{return yield this.botService.getBotDetails(j).toPromise()}catch{return{botName:M.botName,description:"Error loading details"}}})));this.botSections.push({language:t,botDetails:p})}}),error:e=>{console.error("Error fetching bots configuration:",e),this.errorMessage="Failed to load the bot list."}})}static \u0275fac=function(t){return new(t||n)(P(y))};static \u0275cmp=s({type:n,selectors:[["app-bot-list"]],standalone:!0,features:[d],decls:5,vars:2,consts:[["errorTemplate",""],[4,"ngIf","ngIfElse"],[3,"language","bots",4,"ngFor","ngForOf"],[3,"language","bots"]],template:function(t,o){if(t&1&&(g(0,"app-header"),f(1,dt,2,1,"main",1)(2,ft,2,1,"ng-template",null,0,I),g(4,"app-footer")),t&2){let p=T(3);c(),m("ngIf",o.botSections.length)("ngIfElse",p)}},dependencies:[C,F,_,E,H,$],styles:["[_ngcontent-%COMP%]:root{--bg-header: #222;--text-header: #fff;--bg-footer: #111;--text-footer: #ccc;--bg-card: #333;--text-card: #eee;--button-bg: #007acc;--button-text: #fff;--button-hover: #005fa3}body[_ngcontent-%COMP%]{background-color:#121212;color:#e0e0e0;font-family:Arial,sans-serif;margin:0;padding:0}"]})};function gt(n,e){if(n&1&&(r(0,"p"),a(1),i()),n&2){let t=l();c(),b(t.uploadedFile.name)}}function ut(n,e){if(n&1&&(r(0,"div",7)(1,"div",8)(2,"h3"),a(3,"Risultato Conversione:"),i(),r(4,"p"),a(5),i()()()),n&2){let t=l();c(5),b(t.qcmContent)}}var R=class n{uploadedFile=null;qcmContent="";onFileSelected(e){let t=e.target;t.files?.length&&(this.uploadedFile=t.files[0],this.processPDF())}processPDF(){return B(this,null,function*(){this.uploadedFile&&(this.qcmContent=`Elaborazione in corso per ${this.uploadedFile?.name}...`,setTimeout(()=>{this.qcmContent=`Il file ${this.uploadedFile?.name} \xE8 stato convertito correttamente in formato QCM!`},1e3))})}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=s({type:n,selectors:[["app-pdf-to-qcm"]],standalone:!0,features:[d],decls:13,vars:2,consts:[[1,"container"],[1,"hero-section"],[1,"upload-section"],["type","file","id","fileInput","accept",".pdf",1,"hidden-input",3,"change"],["for","fileInput",1,"custom-button"],[4,"ngIf"],["class","output-section",4,"ngIf"],[1,"output-section"],[1,"output-card"]],template:function(t,o){t&1&&(g(0,"app-header"),r(1,"div",0)(2,"div",1)(3,"h1"),a(4,"PDF to QCM Converter"),i(),r(5,"p"),a(6,"Carica un file PDF per convertirlo in un formato QCM innovativo."),i()(),r(7,"div",2)(8,"input",3),u("change",function(M){return o.onFileSelected(M)}),i(),r(9,"label",4),a(10,"Choose File"),i(),f(11,gt,2,1,"p",5),i(),f(12,ut,6,1,"div",6),i()),t&2&&(c(11),m("ngIf",o.uploadedFile),c(),m("ngIf",o.qcmContent))},dependencies:[C,_,E],styles:[".container[_ngcontent-%COMP%]{max-width:800px;margin:2rem auto;padding:2rem;background:var(--container-bg, #1f1f1f);border-radius:12px;box-shadow:0 4px 6px #00000080}.hero-section[_ngcontent-%COMP%]{text-align:center;color:#f0f0f0;margin-bottom:2rem}.hero-section[_ngcontent-%COMP%]   h1[_ngcontent-%COMP%]{font-size:2.5rem;margin-bottom:.5rem}.hero-section[_ngcontent-%COMP%]   p[_ngcontent-%COMP%]{font-size:1.1rem;opacity:.8}.upload-section[_ngcontent-%COMP%]{text-align:center;margin-bottom:2rem}.upload-section[_ngcontent-%COMP%]   .file-upload-container[_ngcontent-%COMP%]{display:flex;align-items:center;justify-content:flex-start}.upload-section[_ngcontent-%COMP%]   .hidden-input[_ngcontent-%COMP%]{display:none}.upload-section[_ngcontent-%COMP%]   p[_ngcontent-%COMP%]{margin:.5rem 0;font-size:.9rem;color:#ddd}.output-section[_ngcontent-%COMP%]{background:var(--output-bg, #333);color:#ddd;padding:1rem;border-radius:8px;box-shadow:0 3px 5px #00000080;text-align:center}.output-section[_ngcontent-%COMP%]   .output-card[_ngcontent-%COMP%]{padding:1rem;border-radius:8px;background:var(--output-card-bg, #555)}"]})};var ot=[{path:"",component:Q},{path:"pdf-to-qcm",component:R},{path:"**",loadComponent:()=>import("./chunk-3V3DFFIB.mjs").then(n=>n.NotFoundComponent)}];var it={providers:[et(ot),K(),W()]};var Ct={providers:[X()]},rt=J(it,Ct);var bt=()=>Z(N,rt),pe=bt;export{pe as a};
