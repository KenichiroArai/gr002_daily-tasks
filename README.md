# gr002_daily-tasks

日々のタスク（GitHub Issue、1 日 1 Issue）の「持ち越し」を集計する収集ツール（Java）です。
[daily-tasks-git](https://github.com/KenichiroArai/daily-tasks-git) の Issue を GitHub REST API で取得・解析し、daily-tasks-git の `docs/data/` に JSON を出力します。

## 関連リポジトリ

ローカルでは、次の 4 つを同じフォルダの下に兄弟ディレクトリとして置きます。

| リポジトリ                                                                                | 役割                                                                             |
| ----------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------- |
| [daily-tasks-git](https://github.com/KenichiroArai/daily-tasks-git)                       | 基軸。Issue、ワークフロー、設定ファイル（`config/`）、出力データ（`docs/data/`） |
| gr002_daily-tasks（本リポジトリ）                                                         | 収集ツール（Java 25 / Maven / Spring Boot 4 / Jackson 3）                        |
| [mk002_daily-tasks_web-next](https://github.com/KenichiroArai/mk002_daily-tasks_web-next) | 画面（Next.js + TypeScript + Recharts）                                          |
| [kb002_daily-tasks_doc](https://github.com/KenichiroArai/kb002_daily-tasks_doc)           | 設計書と横断的なドキュメント（Issue 解析の仕様など）                             |

## ビルドと実行

Java 25 と Maven が必要です。

```bash
# テスト（JaCoCo 行・分岐 100% チェック）と実行可能 jar の作成
mvn clean package

# 収集は daily-tasks-git をカレントにして実行します
cd ../daily-tasks-git

# 収集（差分モード）: 最新 10 件の Issue は毎回解析し直し、それより前は保存済みの JSON と更新日時が同じなら解析しない
java -jar ../gr002_daily-tasks/target/gr002_daily-tasks-0.1.0.jar

# 収集（全件モード）: #1 から最新まで解析し直す
java -jar ../gr002_daily-tasks/target/gr002_daily-tasks-0.1.0.jar --full
```

- 環境変数 `GITHUB_TOKEN` を設定すると GitHub API の認証に使います（未設定でも公開リポジトリなら動きます）。
- 解析ルールや標準時間を変えたときは、全件モードで JSON を作り直してください。
- 対象リポジトリ・トークン（`${GITHUB_TOKEN:}`）・出力先・標準時間の設定ファイル・毎回解析し直す最新の件数は [`src/main/resources/application.properties`](src/main/resources/application.properties) の `carryover.*`、使い方やログ・例外のメッセージは [`src/main/resources/messages.properties`](src/main/resources/messages.properties) で管理しています。
- 設定値は Spring Boot の仕組みにより環境変数で上書きできます（例: `CARRYOVER_RECENTCOUNT=20`）。コマンドライン引数は `--full` と `--help` だけを受け付けます。
- Issue 解析の仕様は kb002_daily-tasks_doc の [docs/carryover-spec.md](https://github.com/KenichiroArai/kb002_daily-tasks_doc/blob/main/docs/carryover-spec.md) を参照してください。

## Eclipse

Java 25 に対応した Eclipse（2025-09 以降、m2e を含む「Eclipse IDE for Java Developers」など）と JDK 25 が必要です。JDK 25 は「設定」→「Java」→「インストール済みの JRE」に登録し、「実行環境」の `JavaSE-25` に割り当ててください。

1. 「ファイル」→「インポート」→「Maven」→「既存 Maven プロジェクト」でリポジトリのルート（`pom.xml`）を選びます（「一般」→「既存プロジェクトをワークスペースへ」でも読み込めます）。
2. 読み込み後にエラーが出る場合は、プロジェクトを右クリック →「Maven」→「プロジェクトの更新」を実行します。`pom.xml` を変更したときも同じです。

リポジトリには Eclipse の設定（`.project`、`.classpath`、`.settings/`）を含めています。文字コード UTF-8、改行 LF、Java 25、メソッドの引数名の保持（`-parameters`。Spring Boot の設定のバインドに必要）を設定済みです。

[`launch/`](launch/) に実行の構成を置いています。「実行」→「実行の構成」またはツールバーの実行メニューから選べます。

| 実行の構成                        | 内容                                                         |
| --------------------------------- | ------------------------------------------------------------ |
| daily-tasks（差分モード）         | 収集（差分モード）。作業ディレクトリは `../daily-tasks-git`  |
| daily-tasks（全件モード）         | 収集（全件モード、`--full`）。作業ディレクトリは同上         |
| daily-tasks（mvn clean package）  | テスト（JaCoCo 100% チェック）と実行可能 jar の作成          |

- 収集の実行の構成は、daily-tasks-git が兄弟ディレクトリにあることを前提にしています。
- 環境変数 `GITHUB_TOKEN` は Eclipse を起動した環境から引き継ぎます。必要なら実行の構成の「環境」タブで設定してください（実行の構成のファイルには書かないでください）。
- テストはプロジェクトや `src/test/java` を右クリック →「実行」→「JUnit テスト」で実行できます。カバレッジのチェックは `mvn clean package` の実行の構成で行います。
