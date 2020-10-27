# LegacyEx

## Setup

1. レポジトリをクローン
   
   ```bash
   git clone https://github.com/autotaker/LegacyEx.git
   ```
2. Eclipseを起動し `File > Import > Gradle > Exsting Gradle Project` からプロジェクトをインポート
3. プロジェクトを右クリックして `Run As > Run on Server` から起動
   - Server選択画面ではTomcat 8.5を選択
4. [http://localhost:8080/LegacyEx/legacy/](http://localhost:8080/LegacyEx/legacy/)　にアクセス
   （末尾のスラッシュがないとうまく動かないので注意!）

## 課題
ログイン画面に表示される挨拶メッセージを変更するオプション機能を追加してください。

### 仕様
- サーバ設定の `BIRTHDAY_GREETING_MESSAGE`が `1`の時にオプションが有効化される。
- ユーザの誕生日のときには `Happy Birthday ${username} san`と表示する（ `${username}`はユーザ名に置換される)
- ユーザの誕生日でない時には`Nice day ${username} san`と表示する
- ユーザの言語設定に応じて表示メッセージの言語を変える
  - 日本語の場合は `誕生日おめでとう ${username} さん`, `ごきげんよう ${username} さん`

## CheatSheet

### Sprout method/class
レガシーコードに新規機能を追加したいが、追加するメソッドに仕様化テストがなく、新たに書くのも難しい場合に使えるテクニックです。

1. 変更したい部分だけを別メソッドに切り出す。(Sprout Method)
   - 変更対象クラスのインスタンスをテスト時に生成できない場合、新規クラスを作成し抽出したメソッドを移動する。(Sprout Class)
2. 抽出したメソッドに仕様化テストを書く
3. メソッドをテスト駆動開発で修正する。

### Singletonのテスト方法
対象コード中にシングルトンクラスを利用している場合、そのままではモックすることは難しいです。

```java
public void someMethod(..) {
  String option = GlobalConfig.instance().get("PARAMETER", "0");
}
```

このような場合は、シングルトンの制約をテスト時に緩め、モックインスタンスに入れ替えれるようにしましょう。

1. シングルトンクラスの`final`修飾を除去する
2. シングルトンクラスに`setInstance(Singleton obj)`メソッドを追加する。
   
   このメソッドはテスト時にのみ使うということを明示するために `@VisibleForTesting`アノテーションをつけましょう。
   
   ```java
   @VisibleForTesting
   public static void setInstance(GlobalConfig obj) {
     _instance = obj;
   }
3. テストの `setUp()`メソッドで、モックインスタンスに差し替える
   
   ```java
   @Mock
   GlobalConfig globalConfig;
   
   @Before
   public void setUp() {
     ...
     GlobalConfig.setInstance(globalConfig);
   }
   ```

4. **重要** `tearDown()`メソッドで `setInstance(null)`を呼び出す。
   
   これをしないと他クラスのテスト時にモックインスタンスが残っていて意図しない挙動を引き起こす場合があります。
   
   ```java
   
   @After
   public void tearDown() {
     GlobalConfig.setInstance(null);
   }
   ```
