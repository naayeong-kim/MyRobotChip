# My Robot: Chip

LEGO EV3 Mindstorms 부품을 사용하여 로봇을 만들고, Java를 프로그래밍을 통해 로봇이 환경 내에서 작동하고 상호 작용할 수 있도록 설계한 프로젝트입니다. 시각적 센서를 활용한 총 4개의 테스트가 로봇에게 주어졌으며 Chip은 모든 테스트를 성공적으로 수행하였습니다. 이 프로젝트에서는 로봇의 생태학적 틈새(ecological niche)를 설명하고 디자인 선택과 다양한 문제에서 파생된 문제에 대한 솔루션을 설명합니다. 결과적으로 로봇의 성능과 일반적인 경험에 대한 결론을 얻을 수 있습니다.

이 프로젝트는 Eclipse IDE에서 Java을 이용하여 진행되었으며, 자세한 분석 방법 및 결과는 다음과 같은 추가 코드 파일 및 리포트에서 확인하실 수 있습니다.<br/> 
- [(해당 연구에 대한 code 보러가기)](project.ipynb) <br/>
- [(해당 연구에 대한 full report 보러가기)](report.pdf) <br/> 

## Design

### Basic robot design

[가이드라인](https://le-www-live-s.legocdn.com/sc/media/lessons/mindstorms-ev3/building-instructions/ev3-rem-driving-base-79bebfc16bd491186ea9c9069842155e.pdf)을 참조하여 LEGO EV3 Mindstorms 부품으로 기본적인 로봇을 설계하였습니다. 이 디자인은 대부분의 무게가 로봇 뒤쪽에 있기 때문에 매우 안정적입니다. 이후, 컬러 센서와 그리퍼, 자이로스코프의 부착이 가능하도록 구조를 조정하였습니다. 추가적으로, 로봇이 경사면에 있을 때 더 높은 안정성을 보장하기 위해 뒤쪽에 구조를 추가하여 질량 중심을 더 뒤쪽으로 보냈습니다.
<img src="img/chip.png" width="500" height="500"/> <br/>


### Basic algorithm design

Line following 알고리즘과 Friend or Foe 알고리즘을 제작하였습니다. 자세한 구현 방법은 full report를 참조하세요.


## Challenges

### 1. Explore your island 
챌린지 1의 환경은 그리드, 미로 및 두 개의 기둥으로 구성됩니다. 그리드는 가운데에 십자가 형태로 4분할이 된 정사각형으로 검정색 배경에 흰색 선으로 구성되어 있습니다. 그리드에는 각 모서리와 중앙에 총 5개의 체크포인트가 있습니다. 이 작업에서 가장 중요한 측면은 라인을 얼마나 잘 따라가느냐이기 때문에, 아래 표면의 광도를 감지하기 위해 컬러 센서를 사용합니다. 추가로, 자이로스코프를 사용하여 얼마나 많은 모서리를 통과했는지 감지하여, 그리드에서 이미 방문한 지점에 대한 지식을 저장합니다. <br/><br/>
로봇이 수행해야 할 작업은 다음과 같습니다: <br/>
- 그리드의 모든 체크포인트를 방문한 후 첫 번째 기둥으로 가서 반응합니다.  <br/>
- 다음, 미로의 라인을 따라 2개의 체크포인트를 거쳐 두 번째 기둥에 도달하여 반응합니다. 
<br/><br/>

{Task = line following, Robot = hasColorSensor, Environment = Grid}
{Task = counting corners, Robot = hasGyroscope, Environment = Grid}
{Task = going to the other side of the line , Robot = hasColorSensor, Environment = white line} {Task = findObject, Robot = hasUltrasonicSensor , Environment = middle of the grid, pillar} {Task = reactToPillar Robot = hasUltrasonicSensor , Environment = Pillar}
{Task = solveMaze, Robot = hasColorSensor, Environment = Maze}


### 2. Run for your life 


### 3. Friend or Foe 


### 4. Search & Rescue 




