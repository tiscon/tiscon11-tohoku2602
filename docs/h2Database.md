H2 Databaseに登録したデータを確認する
------------------------------------------

### 前提
- GitHub Codespacesを使い、アプリケーションが起動していること
- ブラウザでアプリケーションにアクセスしていること

### 手順

1. トップページに遷移する
   - 「tiscon insurance company」のトップページを表示させる。
1. H2データベースコンソールをブラウザで開く
   - URLの末尾に `h2-console` を入力する
      - 例：`https://xxxxx-xxxxx-xxxxx-9080.app.github.dev/h2-console/`
1. JDBC URL/User Name/Passwordを以下の通り入力し、「Connect」を押す。
   - JDBC URL : `jdbc:h2:file:./target/db/tiscon`
   - User Name : `sa`
   - Password : 　※Passwordは何も入力をしないで空欄にする。
1. 左側のペインのテーブル名をクリックすると、クリックしたテーブルに対するSELECT文が生成される。
2. `Run`ボタンをクリックすると、生成したSELECT文が実行され、テーブルのデータを確認することができる。
