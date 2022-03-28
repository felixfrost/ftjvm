const avatar = [[1, 2, 3, 4, 5], [6,7,8,9,10], [11, 12,13,14,15], [16,17,18,19,20]];
const images = document.querySelector(".avatars")
let number = 0
renderChoice(number)

function changeImg(number) {

const allImg = document.querySelectorAll(".menu-item")
console.log("Hey! " + number)

}

function renderChoice(number) {
    let html = ``
    if(number == null) {
        number = 0
    }

    let more = number + 1
    let list = avatar[number]

    for(let num of list) {
        html += `<a href="/settings/${num}" th:href="/settings/${num}" class="menu-item "> <img class="profilepic" src="/IMG/Avatar/${num}.png" onclick="changeImg(${num})"></a>`
    }
    html += `<a href="#" class="menu-item "> <img class="profilepic" src="/IMG/Avatar/arrow.png" onclick="changeNumber(${more})"></a>`
    images.innerHTML = html
}

function changeNumber(amt) {
    number = amt
    if (number == avatar.length) {
        number = 0
    }
    renderChoice(number)
}