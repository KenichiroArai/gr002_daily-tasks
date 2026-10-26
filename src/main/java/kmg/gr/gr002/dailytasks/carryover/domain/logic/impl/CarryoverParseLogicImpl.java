package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverParseLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.carryover.domain.types.CarryoverParsePatternTypes;
import kmg.gr.gr002.dailytasks.carryover.domain.types.CarryoverParseTextTypes;
import kmg.gr.gr002.dailytasks.carryover.domain.types.TargetSectionTypes;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002LogMsgTypes;

/**
 * 日々のタスク Issue から持ち越し項目を解析する実装<br>
 * <p>
 * 対象セクションは「負債」「繰り越し」「持ち越し」。未チェック・チェック済みの両方を対象とする。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Component
@Scope("prototype")
public class CarryoverParseLogicImpl implements CarryoverParseLogic {

    /**
     * チェックボックスの行（- [ ] 内容 / - [x] 内容）
     *
     * @since 0.1.0
     */
    private static final Pattern CHECKBOX = Pattern.compile(CarryoverParsePatternTypes.CHECKBOX.getPattern());

    /**
     * 持ち越し元の日付を含む内容（項目名YYYY/MM/DD残り）
     *
     * @since 0.1.0
     */
    private static final Pattern DATED_CONTENT = Pattern.compile(CarryoverParsePatternTypes.DATED_CONTENT.getPattern());

    /**
     * 残数（残：N）
     *
     * @since 0.1.0
     */
    private static final Pattern DECLARED_COUNT
        = Pattern.compile(CarryoverParsePatternTypes.DECLARED_COUNT.getPattern());

    /**
     * 見出し（## 見出し）
     *
     * @since 0.1.0
     */
    private static final Pattern HEADING = Pattern.compile(CarryoverParsePatternTypes.HEADING.getPattern());

    /**
     * ロガー
     *
     * @since 0.1.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CarryoverParseLogicImpl.class);

    /**
     * 括弧内の時間表記
     *
     * @since 0.1.0
     */
    private static final Pattern PARENTHESES = Pattern.compile(CarryoverParsePatternTypes.PARENTHESES.getPattern());

    /**
     * 残り時間の表記（残り時間：N分 / 残りN分 / 先行分残りN分）
     *
     * @since 0.1.0
     */
    private static final Pattern REMAINING_MINUTES
        = Pattern.compile(CarryoverParsePatternTypes.REMAINING_MINUTES.getPattern());

    /**
     * 時間だけの表記（N分）
     *
     * @since 0.1.0
     */
    private static final Pattern SIMPLE_MINUTES
        = Pattern.compile(CarryoverParsePatternTypes.SIMPLE_MINUTES.getPattern());

    /**
     * タイトルの日付（YYYY年MM月DD日）
     *
     * @since 0.1.0
     */
    private static final Pattern TITLE_DATE = Pattern.compile(CarryoverParsePatternTypes.TITLE_DATE.getPattern());

    /**
     * 項目名の末尾の開き括弧
     *
     * @since 0.1.0
     */
    private static final Pattern TRAILING_OPEN_PARENTHESES
        = Pattern.compile(CarryoverParsePatternTypes.TRAILING_OPEN_PARENTHESES.getPattern());

    /**
     * 項目名の末尾の括弧書き
     *
     * @since 0.1.0
     */
    private static final Pattern TRAILING_PARENTHESES
        = Pattern.compile(CarryoverParsePatternTypes.TRAILING_PARENTHESES.getPattern());

    /**
     * 対象セクションの場合にセクション名を追加する<br>
     *
     * @since 0.1.0
     *
     * @param sections
     *                 対象セクション名の一覧
     * @param section
     *                 セクション名
     */
    private static void addSection(final List<String> sections, final String section) {

        /* 対象外のセクションか */
        final TargetSectionTypes sectionType = TargetSectionTypes.getEnum(section);

        if (sectionType == TargetSectionTypes.NONE) {

            // 対象外のセクションの場合

            // セクション名を追加しない
            return;

        }

        /* 追加済みのセクションか */
        final boolean contained = sections.contains(section);

        if (contained) {

            // 追加済みのセクションの場合

            // セクション名を追加しない
            return;

        }

        sections.add(section);

    }

    /**
     * 年・月・日を yyyy-MM-dd 形式にする<br>
     *
     * @since 0.1.0
     *
     * @param year
     *              年
     * @param month
     *              月
     * @param day
     *              日
     *
     * @return yyyy-MM-dd 形式の日付
     */
    private static String formatDate(final String year, final String month, final String day) {

        final String result = String.format(CarryoverParseTextTypes.DATE.getText(), Integer.valueOf(year),
            Integer.valueOf(month), Integer.valueOf(day));
        return result;

    }

    /**
     * 項目名を正規化する<br>
     * <p>
     * 末尾の括弧書き（例: 「（15分）」）や閉じられていない開き括弧を取り除く。
     * </p>
     *
     * @since 0.1.0
     *
     * @param name
     *             項目名
     *
     * @return 正規化した項目名。空になる場合は「不明」
     */
    private static String normalizeName(final String name) {

        String result = name.strip();
        String previous;

        do {

            previous = result;
            result = CarryoverParseLogicImpl.TRAILING_PARENTHESES.matcher(result)
                .replaceAll(CarryoverParseTextTypes.EMPTY.getText()).strip();
            result = CarryoverParseLogicImpl.TRAILING_OPEN_PARENTHESES.matcher(result)
                .replaceAll(CarryoverParseTextTypes.EMPTY.getText()).strip();

        } while (!result.equals(previous));

        /* 項目名が残っているか */
        final boolean named = !result.isEmpty();

        if (named) {

            // 項目名が残っている場合

            // 正規化した項目名を返す
            return result;

        }

        result = CarryoverParseTextTypes.UNKNOWN.getText();
        return result;

    }

    /**
     * 時間表記から残り時間（分）を取得する<br>
     * <p>
     * 括弧内の「残り時間：N分」「残りN分」「先行分残りN分」「N分」を解釈する。
     * </p>
     *
     * @since 0.1.0
     *
     * @param text
     *             持ち越し元の日付より後ろの部分
     *
     * @return 残り時間（分）。解釈できない場合は null
     */
    private static Double parseMinutes(final String text) {

        Double result = null;

        /* 括弧があるか */
        final Matcher parenthesesMatcher = CarryoverParseLogicImpl.PARENTHESES.matcher(text);
        final boolean parentheses        = parenthesesMatcher.find();

        if (!parentheses) {

            // 括弧がない場合

            // 残り時間なしとする
            return result;

        }

        final String inner = parenthesesMatcher.group(1);

        /* 残り時間の表記か */
        final Matcher remainingMatcher = CarryoverParseLogicImpl.REMAINING_MINUTES.matcher(inner);
        final boolean remaining        = remainingMatcher.find();

        if (remaining) {

            // 残り時間の表記の場合

            // 残り時間を返す
            result = Double.valueOf(remainingMatcher.group(1));
            return result;

        }

        /* 時間だけの表記か */
        final Matcher simpleMatcher = CarryoverParseLogicImpl.SIMPLE_MINUTES.matcher(inner);
        final boolean simple        = simpleMatcher.matches();

        if (!simple) {

            // 時間だけの表記ではない場合

            // 残り時間なしとする
            return result;

        }

        result = Double.valueOf(simpleMatcher.group(1));
        return result;

    }

    /**
     * タイトルから日付を取得する<br>
     *
     * @since 0.1.0
     *
     * @param title
     *              Issue タイトル
     *
     * @return 日付（yyyy-MM-dd）。取得できない場合は null
     */
    private static String titleDate(final String title) {

        String result = null;

        /* タイトルがないか */
        if (title == null) {

            // タイトルがない場合

            // 日付なしとする
            return result;

        }

        /* タイトルに日付があるか */
        final Matcher matcher = CarryoverParseLogicImpl.TITLE_DATE.matcher(title);
        final boolean dated   = matcher.find();

        if (!dated) {

            // タイトルに日付がない場合

            // 日付なしとする
            return result;

        }

        result = CarryoverParseLogicImpl.formatDate(matcher.group(1), matcher.group(2), matcher.group(3));
        return result;

    }

    /**
     * タイトルから日付を取得する<br>
     *
     * @since 0.1.0
     *
     * @param title
     *              Issue タイトル
     *
     * @return 日付（yyyy-MM-dd）。取得できない場合は null
     */
    @Override
    public String parseTitleDate(final String title) {

        final String result = CarryoverParseLogicImpl.titleDate(title);
        return result;

    }

    /**
     * 本文を行に分割する<br>
     *
     * @since 0.1.0
     *
     * @param body
     *             Issue 本文。null の場合は空とみなす
     *
     * @return 行
     */
    private static List<String> splitLines(final String body) {

        List<String> result = List.of();

        /* 本文がないか */
        if (body == null) {

            // 本文がない場合

            // 空の行一覧を返す
            return result;

        }

        result = List.of(body.split(CarryoverParsePatternTypes.LINE_SEPARATOR.getPattern(), -1));
        return result;

    }

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    private final MessageLogic messageLogic;

    /**
     * 時間表記がない行を補完する項目ごとの標準時間
     *
     * @since 0.1.0
     */
    private DefaultMinutesModel defaultMinutes;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param messageLogic
     *                     メッセージの取得
     */
    public CarryoverParseLogicImpl(final MessageLogic messageLogic) {

        this.messageLogic = messageLogic;

    }

    /**
     * 標準時間を受け取り、フィールドに保持する<br>
     *
     * @since 0.1.0
     *
     * @param defaultMinutes
     *                       時間表記がない行を補完する項目ごとの標準時間
     */
    @SuppressWarnings("hiding")
    @Override
    public void initialize(final DefaultMinutesModel defaultMinutes) {

        this.defaultMinutes = defaultMinutes;

    }

    /**
     * Issue を解析する<br>
     *
     * @since 0.1.0
     *
     * @param issue
     *              日々のタスク Issue
     *
     * @return 持ち越しの解析結果
     */
    @Override
    public CarryoverIssueModel parse(final DailyTaskIssueModel issue) {

        CarryoverIssueModel result = null;

        /* 日付の取得 */
        final String date = CarryoverParseLogicImpl.titleDate(issue.getTitle());

        /* 日付が取得できたか */
        if (date == null) {

            // 日付が取得できない場合

            // タイトルの日付がないことを警告する
            final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12002);
            CarryoverParseLogicImpl.LOGGER.warn(message, issue.getNumber(), issue.getTitle());

        }

        /* 本文の解析 */
        final List<String>             sections       = new ArrayList<>();
        final List<CarryoverItemModel> items          = new ArrayList<>();
        Integer                        declaredCount  = null;
        String                         currentSection = CarryoverParseTextTypes.EMPTY.getText();

        for (final String line : CarryoverParseLogicImpl.splitLines(issue.getBody())) {

            /* 見出しか */
            final Matcher headingMatcher = CarryoverParseLogicImpl.HEADING.matcher(line);
            final boolean heading        = headingMatcher.matches();

            if (heading) {

                // 見出しの場合

                // 対象セクションかを判定して、次の行へ進む
                currentSection = headingMatcher.group(1);
                CarryoverParseLogicImpl.addSection(sections, currentSection);
                continue;

            }

            /* 対象外のセクションか */
            final TargetSectionTypes sectionType = TargetSectionTypes.getEnum(currentSection);

            if (sectionType == TargetSectionTypes.NONE) {

                // 対象外のセクションの場合

                // 次の行へ進む
                continue;

            }

            /* 残数の行か */
            final Matcher declaredMatcher = CarryoverParseLogicImpl.DECLARED_COUNT.matcher(line);
            final boolean declared        = declaredMatcher.matches();

            if (declared) {

                // 残数の行の場合

                // 残数を控えて、次の行へ進む
                declaredCount = Integer.valueOf(declaredMatcher.group(1));
                continue;

            }

            /* チェックボックスの行か */
            final Matcher checkboxMatcher = CarryoverParseLogicImpl.CHECKBOX.matcher(line);
            final boolean checkbox        = checkboxMatcher.matches();

            if (!checkbox) {

                // チェックボックスの行ではない場合

                // 想定外の行を警告して、次の行へ進む
                this.warnUnexpectedLine(issue.getNumber(), line);
                continue;

            }

            final String  uncheckedText = CarryoverParseTextTypes.UNCHECKED.getText();
            final boolean checked       = !uncheckedText.equals(checkboxMatcher.group(1));
            items.add(this.parseItem(issue.getNumber(), currentSection, checked, checkboxMatcher.group(2), line));

        }

        /* 残数の検証 */
        this.verifyDeclaredCount(issue.getNumber(), declaredCount, items.size());

        result = new CarryoverIssueModel(issue.getNumber(), issue.getTitle(), date, issue.getState(),
            issue.getUpdatedAt(), sections, declaredCount, items);
        return result;

    }

    /**
     * 持ち越し項目の内容を解析する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *                Issue 番号
     * @param section
     *                セクション名
     * @param checked
     *                チェック済みか
     * @param content
     *                チェックボックスの後ろの内容
     * @param raw
     *                元の行
     *
     * @return 持ち越し項目
     */
    private CarryoverItemModel parseItem(final int number, final String section, final boolean checked,
        final String content, final String raw) {

        CarryoverItemModel result = null;

        /* 項目名・持ち越し元の日付・残りの部分の分割 */
        String        namePart     = content;
        String        originDate   = null;
        String        rest         = content;
        final Matcher datedMatcher = CarryoverParseLogicImpl.DATED_CONTENT.matcher(content);

        /* 持ち越し元の日付があるか */
        final boolean dated = datedMatcher.matches();

        if (dated) {

            // 持ち越し元の日付がある場合

            // 項目名、日付、残りの部分に分ける
            namePart = datedMatcher.group(1);
            originDate = CarryoverParseLogicImpl.formatDate(datedMatcher.group(2), datedMatcher.group(3),
                datedMatcher.group(4));
            rest = datedMatcher.group(5);

        } else {

            // 持ち越し元の日付がない場合

            // 日付の形式が違うことを警告する
            final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12003);
            CarryoverParseLogicImpl.LOGGER.warn(message, number, raw);

        }

        final String name = CarryoverParseLogicImpl.normalizeName(namePart);

        /* 項目名が不明か */
        final String  unknownText = CarryoverParseTextTypes.UNKNOWN.getText();
        final boolean unknownName = unknownText.equals(name);

        if (unknownName) {

            // 項目名が不明な場合

            // 項目名が取れないことを警告する
            final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12004);
            CarryoverParseLogicImpl.LOGGER.warn(message, number, raw);

        }

        /* 残り時間の取得 */
        final Double parsedMinutes = CarryoverParseLogicImpl.parseMinutes(rest);

        /* 残り時間を解釈できたか */
        if (parsedMinutes != null) {

            // 残り時間を解釈できた場合

            // 本文の時間を使って項目を返す
            result = new CarryoverItemModel(name, originDate, checked, parsedMinutes, MinutesSourceTypes.PARSED,
                section, raw);
            return result;

        }

        /* 解釈できない残りがあるか */
        final boolean restLeft = !rest.isBlank();

        if (restLeft) {

            // 解釈できない残りがある場合

            // 時間表記を解釈できないことを警告する
            final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12005);
            CarryoverParseLogicImpl.LOGGER.warn(message, number, raw);

        }

        /* 標準時間での補完 */
        final Double defaultValue = this.defaultMinutes.find(name);

        /* 標準時間があるか */
        if (defaultValue == null) {

            // 標準時間がない場合

            // 取得元を不明にして項目を返す
            final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12006);
            CarryoverParseLogicImpl.LOGGER.warn(message, number, name);
            result = new CarryoverItemModel(name, originDate, checked, 0, MinutesSourceTypes.UNKNOWN, section, raw);
            return result;

        }

        result
            = new CarryoverItemModel(name, originDate, checked, defaultValue, MinutesSourceTypes.DEFAULT, section, raw);
        return result;

    }

    /**
     * 本文の残数と解析件数の食い違いを警告する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *                      Issue 番号
     * @param declaredCount
     *                      本文の「残：N」の値。記載がない場合は null
     * @param parsedCount
     *                      解析件数
     */
    private void verifyDeclaredCount(final int number, final Integer declaredCount, final int parsedCount) {

        /* 残数の記載がないか */
        if (declaredCount == null) {

            // 残数の記載がない場合

            // 食い違いを見ない
            return;

        }

        /* 残数と解析件数が一致するか */
        final int declared = declaredCount;

        if (declared == parsedCount) {

            // 残数と解析件数が一致する場合

            // 警告しない
            return;

        }

        final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12008);
        CarryoverParseLogicImpl.LOGGER.warn(message, number, declaredCount, parsedCount);

    }

    /**
     * 対象セクション内の想定外の行を警告する<br>
     * <p>
     * 空行と区切り線（---）は警告しない。
     * </p>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     * @param line
     *               行
     */
    private void warnUnexpectedLine(final int number, final String line) {

        final String stripped = line.strip();

        /* 空行か */
        final boolean blank = stripped.isEmpty();

        if (blank) {

            // 空行の場合

            // 警告しない
            return;

        }

        /* 区切り線か */
        final String  horizontalRule = CarryoverParsePatternTypes.HORIZONTAL_RULE.getPattern();
        final boolean rule           = stripped.matches(horizontalRule);

        if (rule) {

            // 区切り線の場合

            // 警告しない
            return;

        }

        final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12007);
        CarryoverParseLogicImpl.LOGGER.warn(message, number, line);

    }

}
