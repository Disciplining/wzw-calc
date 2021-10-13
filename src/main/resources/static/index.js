let y = 400; // 固定y坐标

$
(
    function()
    {
        submitBtnBind()
    }
);


/**
 * 提交按钮绑定事件
 */
function submitBtnBind()
{
    $('#submitBtn').bind
    (
        'click',
        function ()
        {
            let files = $('#fileInput').prop('files');
            let data = new FormData();
            data.append('file', files[0]);

            $.ajax
            (
                {
                    url : '/upload-calc',
                    type : 'POST',
                    dataType : 'json',
                    data: data,
                    cache: false,
                    processData: false,
                    contentType: false,
                    success : function (resp)
                    {
                        let dataList = resp.data; // 坐标数据
                        drawUp(dataList);
                    },
                    error : function ()
                    {
                        console.log('请求失败');
                    }
                }
            );
        }
    );
}

/**
 * 画整个图像
 * @param dataList 坐标数据
 */
function drawUp(dataList)
{
    console.log("这是列表数据：", dataList);

    let canvasDom = document.getElementById("myCanvas");
    let context = canvasDom.getContext("2d");

    // 画坐标
    drawLine(context);
    for (let el of dataList)
    {
        drawOneCircle(el.index, el.r, context);
    }
}





/**
 * 画一维坐标轴
 * @param crx 画布上下文
 */
function drawLine(ctx)
{
    // ①画线
    ctx.beginPath();
    ctx.moveTo(0, y);
    ctx.lineTo(3000, y);
    ctx.closePath();
    ctx.strokeStyle = "black";
    ctx.stroke();

    // ②原点
    basisDrawPoint(1, true, ctx);

    // ③原点写0
    basisDrawText('0', 0, ctx);
}

/**
 * 画一个圆
 * @param x   圆心x坐标
 * @param r   半径
 * @param ctx 画布上下文
 */
function drawOneCircle(x, r, ctx)
{
    // ①画圆形
    ctx.beginPath();
    ctx.arc(x, y, r, 0, 2*Math.PI);
    ctx.lineWidth = 1;
    ctx.stroke();
    ctx.closePath();

    // ②画圆心
    basisDrawPoint(x, false, ctx);

    // ③写圆心坐标
    basisDrawText(x, x, ctx);

    // ④写最右边坐标
    basisDrawText(x+r, x+r, ctx);
}

/**
 * 基础文方法，写文字.
 * @param content 要写的文字
 * @param x       x坐标
 * @param ctx     画面上下文
 */
function basisDrawText(content, x, ctx)
{
    ctx.beginPath();
    ctx.fillStyle = 'red';
    ctx.fillText(content, x+2, y-3);
    ctx.closePath();
}

/**
 * 基础方法，画一个点.
 * @param x       x坐标，当是原点时这个参数不起作用.
 * @param isFirst 是否是原点，true-是，false-不是.
 * @param ctx     画布上下文
 */
function basisDrawPoint(x, isFirst, ctx)
{
    ctx.beginPath();
    ctx.arc(isFirst?2:x, y, 3, 0, 2*Math.PI);
    ctx.closePath();
    ctx.lineWidth = 1;
    ctx.fillStyle = 'black';
    ctx.fill();
}
