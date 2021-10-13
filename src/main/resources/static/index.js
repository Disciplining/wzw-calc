let y = 400; // 固定y坐标

$
(
    function()
    {
        // drawUp();
    }
);



function getData()
{
    $.ajax
    (
        {
            url : '/ajaxTest', //请求的url
            type : 'GET', //以何种方法发送报文
            dataType : 'json', //预期的服务器返回的数据类型
            headers:
            {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' //发送的报文的MIME类型
            },
            success : function () //请求成功执行的访求
            {
                console.log('请求成功');
            },
            error : function () //请求失败执行的方法
            {
                console.log('请求失败');
            }
        }
    );
}




function drawUp()
{
    let canvasDom = document.getElementById("myCanvas");
    let context = canvasDom.getContext("2d");

    drawLine(context);
    drawOneCircle(275, 88, context);
    drawOneCircle(150, 50, context);
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
